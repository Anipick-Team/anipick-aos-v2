package com.jparkbro.community.impl.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.component.AniPickDropdownMenuIcon
import com.jparkbro.core.designsystem.icon.ArrowRight
import com.jparkbro.core.designsystem.icon.Comment
import com.jparkbro.core.designsystem.icon.MoreHorizontal
import com.jparkbro.core.designsystem.model.AniPickDropdownMenuItem
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.ui.component.AniPickProfileNickname

/** 댓글 한 건 - 좋아요/답글 액션과 대댓글([CommunityComment.replies])까지 함께 그린다. */
@Composable
internal fun CommunityCommentItem(
    comment: CommunityComment,
    onReplyClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(AniPickTheme.colors.lightGray),
    ) {
        CommentBody(
            comment = comment,
            onReplyClick = { onReplyClick(comment.commentId) },
            onLikeClick = { onLikeClick(comment.commentId) },
            onEditClick = { onEditClick(comment.commentId) },
            onDeleteClick = { onDeleteClick(comment.commentId) },
            onReportClick = { onReportClick(comment.commentId) },
        )
        comment.replies?.forEach { reply ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(
                    imageVector = ArrowRight,
                    contentDescription = null,
                    tint = AniPickTheme.colors.textGray,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 16.dp)
                        .size(24.dp),
                )
                CommentBody(
                    comment = reply,
                    onReplyClick = null,
                    onLikeClick = { onLikeClick(reply.commentId) },
                    onEditClick = { onEditClick(reply.commentId) },
                    onDeleteClick = { onDeleteClick(reply.commentId) },
                    onReportClick = { onReportClick(reply.commentId) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/** 댓글/대댓글 공통 본문 - 프로필/닉네임/날짜/본문/좋아요, [onReplyClick]이 있을 때만 답글 액션을 보여준다. */
@Composable
private fun CommentBody(
    comment: CommunityComment,
    onReplyClick: (() -> Unit)?,
    onLikeClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLiked = comment.isLiked == true
    val isDeleted = comment.isDeleted == true
    val isReply = onReplyClick == null

    Column(
        modifier = modifier
            .padding(
                start = if (isReply) 0.dp else 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (!isDeleted) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AniPickProfileNickname(profileImageUrl = comment.profileImageUrl, nickname = comment.nickname ?: "-")
                    AniPickDropdownMenuIcon(
                        items = if (comment.isMine == true) {
                            listOf(
                                commentMenuItem("수정", onEditClick),
                                commentMenuItem("삭제", onDeleteClick),
                            )
                        } else {
                            listOf(commentMenuItem("신고", onReportClick))
                        },
                        trigger = {
                            Icon(
                                imageVector = MoreHorizontal,
                                contentDescription = "더보기",
                                tint = AniPickTheme.colors.black,
                                modifier = Modifier.size(20.dp),
                            )
                        },
                    )
                }
                comment.createdAt?.let { createdAt ->
                    Text(
                        text = if (comment.isEdited == true) "$createdAt (수정됨)" else createdAt,
                        style = AniPickTheme.typography.caption2,
                        color = AniPickTheme.colors.textGray,
                    )
                }
            }
        }
        Column(
            modifier = if (isReply) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
                    .fillMaxWidth()
                    .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            },
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = if (isDeleted) "삭제된 댓글입니다." else comment.content ?: "",
                style = AniPickTheme.typography.body2,
                color = if (isDeleted) AniPickTheme.colors.point else AniPickTheme.colors.black,
                textAlign = if (isDeleted) TextAlign.Center else TextAlign.Start,
                modifier = if (isDeleted) Modifier.fillMaxWidth() else Modifier,
            )
            if (!isDeleted) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AniPickAnimatedHeartIcon(
                            isLiked = isLiked,
                            onClick = onLikeClick,
                            contentDescription = if (isLiked) "좋아요 취소" else "좋아요",
                            unlikedTint = AniPickTheme.colors.textGray,
                            size = 16.dp,
                        )
                        Text(
                            text = "좋아요",
                            style = AniPickTheme.typography.caption1,
                            color = if (isLiked) AniPickTheme.colors.point else AniPickTheme.colors.textGray,
                        )
                    }
                    if (onReplyClick != null) {
                        VerticalDivider(thickness = 1.dp, modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.clickable(onClick = onReplyClick),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Comment,
                                contentDescription = "댓글",
                                tint = AniPickTheme.colors.textGray,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = "댓글",
                                style = AniPickTheme.typography.caption1,
                                color = AniPickTheme.colors.textGray,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun commentMenuItem(label: String, onClick: () -> Unit) = AniPickDropdownMenuItem(
    content = {
        Text(
            text = label,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
        )
    },
    onClick = onClick,
)

@Preview(showBackground = true)
@Composable
private fun CommunityCommentItemPreview() {
    CommunityCommentItem(
        comment = CommunityComment(
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
                CommunityComment(
                    commentId = 3L,
                    nickname = "deleted_user",
                    isDeleted = true,
                    createdAt = "2025. 04. 03",
                ),
            ),
        ),
        onReplyClick = {},
        onLikeClick = {},
        onEditClick = {},
        onDeleteClick = {},
        onReportClick = {},
    )
}
