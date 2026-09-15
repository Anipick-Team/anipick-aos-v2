package com.jparkbro.community.impl.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.community.impl.detail.components.CommunityCommentInputBar
import com.jparkbro.community.impl.detail.components.CommunityCommentItem
import com.jparkbro.community.impl.detail.components.CommunityCommentItemSkeleton
import com.jparkbro.community.impl.detail.components.CommunityDetailHeader
import com.jparkbro.community.impl.detail.components.CommunityDetailSkeleton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickDropdownMenuIcon
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.icon.MoreHorizontal
import com.jparkbro.core.designsystem.model.AniPickDropdownMenuItem
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.ui.component.AniPickReportDialog
import com.jparkbro.core.ui.effect.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val COMMENT_SKELETON_ITEM_COUNT = 3

@Composable
internal fun CommunityDetailRoot(
    postId: Long,
    onBackClick: () -> Unit,
    onNavigateToEdit: (seriesId: Long, postId: Long) -> Unit,
    viewModel: CommunityDetailViewModel = koinViewModel(parameters = { parametersOf(postId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CommunityDetailEvent.DeleteSuccess -> onBackClick()
        }
    }

    CommunityDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CommunityDetailAction.Navigation -> when (action) {
                    CommunityDetailAction.OnBackClick -> onBackClick()
                    CommunityDetailAction.OnEditClick -> {
                        val seriesId = state.post.seriesId
                        if (seriesId != null) onNavigateToEdit(seriesId, postId)
                    }
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CommunityDetailScreen(
    state: CommunityDetailState,
    onAction: (CommunityDetailAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        topBar = {
            AniPickTitleTopAppBar(
                onBackClick = { onAction(CommunityDetailAction.OnBackClick) },
                actions = {
                    AniPickDropdownMenuIcon(
                        items = if (state.post.isMine == true) {
                            listOf(
                                communityMenuItem("수정") { onAction(CommunityDetailAction.OnEditClick) },
                                communityMenuItem("삭제") { onAction(CommunityDetailAction.OnDeleteClick) },
                            )
                        } else {
                            listOf(
                                communityMenuItem("신고") { onAction(CommunityDetailAction.OnReportClick) },
                            )
                        },
                        trigger = {
                            Icon(
                                imageVector = MoreHorizontal,
                                contentDescription = "더보기",
                                tint = AniPickTheme.colors.black,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                    )
                }
            )
        },
        containerColor = AniPickTheme.colors.lightGray,
        bottomBar = {
            if (!state.isContentLoading && state.error == null) {
                CommunityCommentInputBar(
                    state = state.commentInputState,
                    enabled = state.commentInputState.text.isNotBlank() && !state.isCommentSubmitting,
                    isSubmitting = state.isCommentSubmitting,
                    replyTargetContent = state.replyTargetComment?.content,
                    isEditing = state.editTargetComment != null,
                    onCancelClick = {
                        if (state.editTargetComment != null) {
                            onAction(CommunityDetailAction.OnCommentEditCancelClick)
                        } else {
                            onAction(CommunityDetailAction.OnReplyTargetCancelClick)
                        }
                    },
                    onSendClick = { onAction(CommunityDetailAction.OnCommentSendClick) },
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = BottomEdgeShadowClearance),
            contentPadding = PaddingValues(bottom = 40.dp),
        ) {
            if (state.isContentLoading) {
                item {
                    Column {
                        CommunityDetailSkeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AniPickTheme.colors.white)
                                .padding(top = 40.dp),
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(AniPickTheme.colors.white),
                        )
                    }
                }
                items(COMMENT_SKELETON_ITEM_COUNT) {
                    CommunityCommentItemSkeleton()
                }
            } else if (state.error != null) {
                item {
                    AniPickEmptyState(
                        message = state.error,
                        onRetryClick = { onAction(CommunityDetailAction.OnRetryClick) },
                        modifier = Modifier
                            .fillParentMaxHeight(0.6f)
                            .padding(top = 40.dp),
                    )
                }
            } else {
                item {
                    Column {
                        CommunityDetailHeader(
                            post = state.post,
                            images = state.post.imageUrls.orEmpty().mapIndexed { index, url ->
                                state.post.imageBytesList?.getOrNull(index) ?: url
                            },
                            onLikeClick = { onAction(CommunityDetailAction.OnPostLikeClick) },
                            onShareClick = { onAction(CommunityDetailAction.OnShareClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AniPickTheme.colors.white)
                                .padding(top = 20.dp),
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(AniPickTheme.colors.white),
                        )
                    }
                }
                itemsIndexed(state.comments, key = { _, comment -> comment.commentId }) { index, comment ->
                    Column {
                        CommunityCommentItem(
                            comment = comment,
                            onReplyClick = { onAction(CommunityDetailAction.OnCommentReplyClick(it)) },
                            onLikeClick = { onAction(CommunityDetailAction.OnCommentLikeClick(it)) },
                            onEditClick = { onAction(CommunityDetailAction.OnCommentEditClick(it)) },
                            onDeleteClick = { onAction(CommunityDetailAction.OnCommentDeleteClick(it)) },
                            onReportClick = { onAction(CommunityDetailAction.OnCommentReportClick(it)) },
                        )
                        if (index != state.comments.lastIndex) {
                            HorizontalDivider(thickness = 1.dp, color = AniPickTheme.colors.gray)
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteConfirmDialog) {
        AniPickDialog(
            title = "게시글 삭제",
            message = "이 게시글을 삭제하시겠어요?",
            onDismissRequest = { onAction(CommunityDetailAction.OnDeleteDismiss) },
            confirmText = "삭제",
            onConfirm = { onAction(CommunityDetailAction.OnDeleteConfirm) },
            dismissText = "취소",
            onDismiss = { onAction(CommunityDetailAction.OnDeleteDismiss) },
        )
    }

    if (state.commentDeleteTargetId != null) {
        AniPickDialog(
            title = "댓글 삭제",
            message = "이 댓글을 삭제하시겠어요?",
            onDismissRequest = { onAction(CommunityDetailAction.OnCommentDeleteDismiss) },
            confirmText = "삭제",
            onConfirm = { onAction(CommunityDetailAction.OnCommentDeleteConfirm) },
            dismissText = "취소",
            onDismiss = { onAction(CommunityDetailAction.OnCommentDeleteDismiss) },
        )
    }

    if (state.showReportDialog || state.commentReportTargetId != null) {
        AniPickReportDialog(
            selectedCategory = state.reportCategory,
            onCategorySelect = { onAction(CommunityDetailAction.OnReportCategorySelect(it)) },
            onDismissRequest = { onAction(CommunityDetailAction.OnReportDismiss) },
            onConfirm = { onAction(CommunityDetailAction.OnReportConfirm) },
            onDismiss = { onAction(CommunityDetailAction.OnReportDismiss) },
        )
    }
}

private fun communityMenuItem(label: String, onClick: () -> Unit) = AniPickDropdownMenuItem(
    content = {
        Text(
            text = label,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
        )
    },
    onClick = onClick,
)

@Composable
@Preview(showBackground = true)
private fun CommunityDetailScreenPreview() {
    CommunityDetailScreen(
        state = CommunityDetailState(
            postId = 1L,
            post = CommunityPost(
                postId = 1L,
                nickname = "anipick_x2k3",
                title = "이번 화 진짜 미쳤다",
                content = "이번 화 전개 보고 소름 돋았음... 다들 봤어? 연출이랑 작화 둘 다 미쳤다 진짜.",
                isSpoiler = true,
                viewCount = 102,
                likeCount = 32,
                commentCount = 8,
                createdAt = "2025. 04. 03",
                imageUrls = listOf("", "", ""),
            ),
            comments = listOf(
                CommunityComment(
                    commentId = 1L,
                    nickname = "anipick_x2k3",
                    content = "저도 이번 화 보고 소름 돋았어요 ㅋㅋ",
                    likeCount = 4,
                    createdAt = "2025. 04. 03",
                    replies = listOf(
                        CommunityComment(
                            commentId = 2L,
                            nickname = "another_user",
                            content = "저도요 ㅠㅠ",
                            likeCount = 1,
                            createdAt = "2025. 04. 03",
                        ),
                    ),
                ),
                CommunityComment(
                    commentId = 3L,
                    nickname = "deleted_user",
                    isDeleted = true,
                    createdAt = "2025. 04. 03",
                ),
            ),
        ),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityDetailScreenSkeletonPreview() {
    CommunityDetailScreen(
        state = CommunityDetailState(postId = 1L, isLoading = true),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityDetailScreenErrorPreview() {
    CommunityDetailScreen(
        state = CommunityDetailState(postId = 1L, error = "네트워크 연결을 확인해주세요."),
        onAction = {},
    )
}
