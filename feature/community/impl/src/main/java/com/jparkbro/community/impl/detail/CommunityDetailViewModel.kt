package com.jparkbro.community.impl.detail

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.community.CommunityRepository
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.model.report.ReportTargetType
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityDetailViewModel(
    postId: Long,
    private val communityRepository: CommunityRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityDetailState(postId = postId))
    val state: StateFlow<CommunityDetailState> = _state.asStateFlow()

    private val _events = Channel<CommunityDetailEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadPostDetail()
        loadComments(showLoading = true)
    }

    fun onAction(action: CommunityDetailAction) {
        when (action) {
            is CommunityDetailAction.Navigation -> Unit // Root에서 처리한다.

            // TODO: 댓글 아이콘/공유 기능 API가 추가되면 연동한다. 지금은 자리만 잡아둔다.
            CommunityDetailAction.OnCommentIconClick -> Unit
            CommunityDetailAction.OnShareClick -> Unit
            is CommunityDetailAction.OnCommentReplyClick -> onCommentReplyClick(action.commentId)
            CommunityDetailAction.OnReplyTargetCancelClick -> _state.update { it.copy(replyTargetComment = null) }
            is CommunityDetailAction.OnCommentEditClick -> onCommentEditClick(action.commentId)
            CommunityDetailAction.OnCommentEditCancelClick -> cancelCommentEdit()
            CommunityDetailAction.OnRetryClick -> {
                loadPostDetail()
                loadComments(showLoading = true)
            }
            CommunityDetailAction.OnDeleteClick -> _state.update { it.copy(showDeleteConfirmDialog = true) }
            CommunityDetailAction.OnDeleteConfirm -> onDeleteConfirm()
            CommunityDetailAction.OnDeleteDismiss -> _state.update { it.copy(showDeleteConfirmDialog = false) }
            CommunityDetailAction.OnReportClick -> _state.update { it.copy(showReportDialog = true) }
            is CommunityDetailAction.OnReportCategorySelect -> _state.update { it.copy(reportCategory = action.category) }
            CommunityDetailAction.OnReportConfirm -> onReportConfirm()
            CommunityDetailAction.OnReportDismiss -> dismissReportDialog()
            CommunityDetailAction.OnPostLikeClick -> togglePostLike()
            CommunityDetailAction.OnCommentSendClick -> onCommentSendClick()
            is CommunityDetailAction.OnCommentLikeClick -> toggleCommentLike(action.commentId)
            is CommunityDetailAction.OnCommentDeleteClick -> _state.update { it.copy(commentDeleteTargetId = action.commentId) }
            CommunityDetailAction.OnCommentDeleteConfirm -> onCommentDeleteConfirm()
            CommunityDetailAction.OnCommentDeleteDismiss -> _state.update { it.copy(commentDeleteTargetId = null) }
            is CommunityDetailAction.OnCommentReportClick -> _state.update { it.copy(commentReportTargetId = action.commentId) }
        }
    }

    private fun loadPostDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            communityRepository.getPostDetail(_state.value.postId)
                .onSuccess { post -> _state.update { it.copy(post = post, isLoading = false) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) } }
        }
    }

    /** 실패하면 조용히 넘어간다 - 댓글 없이 게시글만 보여준다(다른 화면의 부가 목록 실패 처리와 동일).
     *  [CommunityDetailState.isContentLoading]이 [loadPostDetail]과 같이 이 로딩도 봐서, 최초 진입 시엔
     *  상세/댓글 둘 다 끝나야 스켈레톤에서 실제 콘텐츠로 바뀐다. 댓글 등록 후 재조회([showLoading]=false)는
     *  이미 콘텐츠가 떠 있는 상태라 스켈레톤으로 안 돌아가고 목록만 조용히 갱신된다. */
    private fun loadComments(showLoading: Boolean) {
        viewModelScope.launch {
            if (showLoading) _state.update { it.copy(isCommentsLoading = true) }

            communityRepository.getComments(_state.value.postId)
                .onSuccess { page -> _state.update { it.copy(comments = page.items ?: emptyList(), isCommentsLoading = false) } }
                .onFailure { _state.update { it.copy(isCommentsLoading = false) } }
        }
    }

    /** 답글 대상 지정 - 대댓글까지 재귀로 뒤져서 대상 댓글을 찾아 미리보기에 쓴다 - 수정 모드였다면 해제 */
    private fun onCommentReplyClick(commentId: Long) {
        val target = findComment(_state.value.comments, commentId) ?: return
        if (_state.value.editTargetComment != null) {
            _state.value.commentInputState.setTextAndPlaceCursorAtEnd("")
        }
        _state.update { it.copy(replyTargetComment = target, editTargetComment = null) }
    }

    /** 수정 모드 진입 - 입력창에 원래 내용을 채워 넣는다. 답글 모드였다면 해제한다. */
    private fun onCommentEditClick(commentId: Long) {
        val target = findComment(_state.value.comments, commentId) ?: return
        _state.update { it.copy(editTargetComment = target, replyTargetComment = null) }
        _state.value.commentInputState.setTextAndPlaceCursorAtEnd(target.content.orEmpty())
    }

    private fun cancelCommentEdit() {
        _state.value.commentInputState.setTextAndPlaceCursorAtEnd("")
        _state.update { it.copy(editTargetComment = null) }
    }

    /** 댓글/답글 등록 또는 수정 - [CommunityDetailState.editTargetComment]가 있으면 수정, 아니면
     *  [CommunityDetailState.replyTargetComment] 유무에 따라 댓글/답글로 등록한다 */
    private fun onCommentSendClick() {
        val current = _state.value
        val content = current.commentInputState.text.toString().trim()
        if (content.isBlank() || current.isCommentSubmitting) return

        val editTargetId = current.editTargetComment?.commentId
        if (editTargetId != null) {
            onCommentEditSendClick(editTargetId, content)
        } else {
            onCommentCreateSendClick(content, current.replyTargetComment?.commentId)
        }
    }

    private fun onCommentCreateSendClick(content: String, parentCommentId: Long?) {
        viewModelScope.launch {
            _state.update { it.copy(isCommentSubmitting = true) }

            communityRepository.createComment(
                postId = _state.value.postId,
                content = content,
                parentCommentId = parentCommentId,
            )
                .onSuccess {
                    _state.value.commentInputState.setTextAndPlaceCursorAtEnd("")
                    _state.update { it.copy(isCommentSubmitting = false, replyTargetComment = null) }
                    globalSnackbarManager.showSnackbar(if (parentCommentId != null) "답글이 등록되었습니다." else "댓글이 등록되었습니다.")
                    loadComments(showLoading = false)
                }
                .onFailure { error ->
                    _state.update { it.copy(isCommentSubmitting = false) }
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
        }
    }

    private fun onCommentEditSendClick(commentId: Long, content: String) {
        viewModelScope.launch {
            _state.update { it.copy(isCommentSubmitting = true) }

            communityRepository.updateComment(commentId, content)
                .onSuccess {
                    _state.value.commentInputState.setTextAndPlaceCursorAtEnd("")
                    _state.update { it.copy(isCommentSubmitting = false, editTargetComment = null) }
                    globalSnackbarManager.showSnackbar("댓글이 수정되었습니다.")
                    loadComments(showLoading = false)
                }
                .onFailure { error ->
                    _state.update { it.copy(isCommentSubmitting = false) }
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
        }
    }

    /** 게시글 좋아요 낙관적 토글 - 실패하면 원상복구 + 스낵바. */
    private fun togglePostLike() {
        val wasLiked = _state.value.post.isLiked == true
        applyPostLikeState(isLiked = !wasLiked)

        viewModelScope.launch {
            val postId = _state.value.postId
            val result = if (wasLiked) communityRepository.unlikePost(postId) else communityRepository.likePost(postId)
            result.onFailure { error ->
                applyPostLikeState(isLiked = wasLiked)
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    private fun applyPostLikeState(isLiked: Boolean) {
        _state.update {
            it.copy(
                post = it.post.copy(
                    isLiked = isLiked,
                    likeCount = (it.post.likeCount ?: 0) + if (isLiked) 1 else -1,
                ),
            )
        }
    }

    private fun onDeleteConfirm() {
        _state.update { it.copy(showDeleteConfirmDialog = false) }

        viewModelScope.launch {
            communityRepository.deletePost(_state.value.postId)
                .onSuccess {
                    // 목록 화면(CommunityMain)이 다시 보일 때 삭제된 게시글이 안 보이도록 캐시를 재조회시킨다.
                    communityRepository.refreshCommunityBoardPosts()
                    globalSnackbarManager.showSnackbar("게시글이 삭제되었습니다.")
                    _events.send(CommunityDetailEvent.DeleteSuccess)
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    /** [CommunityDetailState.commentReportTargetId]가 있으면 그 댓글을, 없으면 게시글을 신고한다 -
     *  게시글/댓글 신고 다이얼로그가 이 상태를 공용으로 쓴다. */
    private fun onReportConfirm() {
        val category = _state.value.reportCategory
        val commentId = _state.value.commentReportTargetId
        dismissReportDialog()
        if (category == null) return

        viewModelScope.launch {
            val result = if (commentId != null) {
                communityRepository.report(ReportTargetType.COMMENT, commentId, category)
            } else {
                communityRepository.report(ReportTargetType.POST, _state.value.postId, category)
            }
            result
                .onSuccess { globalSnackbarManager.showSnackbar("신고가 접수되었습니다.") }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissReportDialog() {
        _state.update { it.copy(showReportDialog = false, commentReportTargetId = null, reportCategory = null) }
    }

    /** 댓글/대댓글 좋아요 낙관적 토글 - 실패하면 원상복구 + 스낵바. */
    private fun toggleCommentLike(commentId: Long) {
        val target = findComment(_state.value.comments, commentId) ?: return
        val wasLiked = target.isLiked == true
        applyCommentLikeState(commentId, isLiked = !wasLiked)

        viewModelScope.launch {
            val result = if (wasLiked) communityRepository.unlikeComment(commentId) else communityRepository.likeComment(commentId)
            result.onFailure { error ->
                applyCommentLikeState(commentId, isLiked = wasLiked)
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    private fun applyCommentLikeState(commentId: Long, isLiked: Boolean) {
        _state.update { state ->
            state.copy(
                comments = mapComment(state.comments, commentId) { comment ->
                    comment.copy(
                        isLiked = isLiked,
                        likeCount = (comment.likeCount ?: 0) + if (isLiked) 1 else -1,
                    )
                },
            )
        }
    }

    private fun onCommentDeleteConfirm() {
        val commentId = _state.value.commentDeleteTargetId
        _state.update { it.copy(commentDeleteTargetId = null) }
        if (commentId == null) return

        viewModelScope.launch {
            communityRepository.deleteComment(commentId)
                .onSuccess {
                    _state.update { it.copy(comments = removeComment(it.comments, commentId)) }
                    globalSnackbarManager.showSnackbar("댓글이 삭제되었습니다.")
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    /** 대댓글까지 재귀로 뒤져서 [commentId]를 찾는다. */
    private fun findComment(comments: List<CommunityComment>, commentId: Long): CommunityComment? {
        for (comment in comments) {
            if (comment.commentId == commentId) return comment
            comment.replies?.let { findComment(it, commentId) }?.let { return it }
        }
        return null
    }

    /** [commentId]에 해당하는 댓글/대댓글 하나만 [transform]으로 바꾼 새 목록을 돌려준다. */
    private fun mapComment(
        comments: List<CommunityComment>,
        commentId: Long,
        transform: (CommunityComment) -> CommunityComment,
    ): List<CommunityComment> {
        return comments.map { comment ->
            if (comment.commentId == commentId) {
                transform(comment)
            } else {
                comment.copy(replies = comment.replies?.let { mapComment(it, commentId, transform) })
            }
        }
    }

    /** [commentId]에 해당하는 댓글/대댓글을 제거한 새 목록을 돌려준다. */
    private fun removeComment(comments: List<CommunityComment>, commentId: Long): List<CommunityComment> {
        return comments.filterNot { it.commentId == commentId }.map { comment ->
            comment.copy(replies = comment.replies?.let { removeComment(it, commentId) })
        }
    }
}
