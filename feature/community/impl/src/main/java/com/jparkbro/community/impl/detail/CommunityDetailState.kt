package com.jparkbro.community.impl.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.report.ReportCategory

data class CommunityDetailState(
    val postId: Long = 0L,
    val post: CommunityPost = CommunityPost(postId = 0L),
    val comments: List<CommunityComment> = emptyList(),
    val isLoading: Boolean = false,
    val isCommentsLoading: Boolean = false,
    val error: String? = null,
    /** 삭제 확인 다이얼로그 표시 여부. */
    val showDeleteConfirmDialog: Boolean = false,
    /** 신고 다이얼로그 표시 여부 - 게시글 신고. */
    val showReportDialog: Boolean = false,
    /** 신고 다이얼로그를 띄울 댓글 - null이 아니면 게시글 대신 이 댓글을 신고한다. */
    val commentReportTargetId: Long? = null,
    /** [showReportDialog]/[commentReportTargetId] 둘 중 뭐가 열려 있든 공용으로 쓰는 선택된 신고 유형. */
    val reportCategory: ReportCategory? = null,
    /** 삭제 확인 다이얼로그를 띄울 댓글 - null이면 다이얼로그를 띄우지 않는다. */
    val commentDeleteTargetId: Long? = null,
    /** 최하단 댓글 입력창. */
    val commentInputState: TextFieldState = TextFieldState(),
    val isCommentSubmitting: Boolean = false,
    /** 답글 대상 댓글 - null이 아니면 입력창이 답글 모드(대상 미리보기 표시, 등록 시 parentCommentId로 전달). */
    val replyTargetComment: CommunityComment? = null,
    /** 수정 대상 댓글 - null이 아니면 입력창이 수정 모드(내용 프리필, 등록 시 updateComment로 전달). */
    val editTargetComment: CommunityComment? = null,
) {
    /** 게시글/댓글 목록이 둘 다 끝나야(둘 중 하나라도 로딩 중이면 false) 실제 콘텐츠를 그린다. */
    val isContentLoading: Boolean
        get() = isLoading || isCommentsLoading
}
