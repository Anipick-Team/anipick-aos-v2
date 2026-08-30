package com.jparkbro.community.impl.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.community.impl.components.PostStat
import com.jparkbro.community.impl.components.PostStatDivider
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.icon.Comment
import com.jparkbro.core.designsystem.icon.HeartOutlined
import com.jparkbro.core.designsystem.icon.Share
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.ui.component.AniPickProfileNickname

/** 게시글 상세 상단 블록 - 작성자/이미지/본문/통계 + 좋아요·댓글·공유 액션. */
@Composable
internal fun CommunityDetailHeader(
    post: CommunityPost,
    images: List<String>,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PostAuthorInfo(post = post, modifier = Modifier.padding(horizontal = 20.dp))

        if (images.isNotEmpty()) {
            CommunityDetailImagePager(
                images = images,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        PostBody(post = post, modifier = Modifier.padding(horizontal = 20.dp))
        AniPickSectionDivider()

        PostActionsRow(
            isLiked = post.isLiked == true,
            onLikeClick = onLikeClick,
            onShareClick = onShareClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        AniPickSectionDivider()
    }
}

@Composable
private fun PostAuthorInfo(post: CommunityPost, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AniPickProfileNickname(profileImageUrl = post.profileImageUrl, nickname = post.nickname)
            if (post.isSpoiler == true) {
                AniPickGenreTag(genre = "스포일러")
            }
        }
        post.createdAt?.let { createdAt ->
            Text(
                text = createdAt,
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Composable
private fun PostBody(post: CommunityPost, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = post.title ?: "-",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
        )
        Text(
            text = post.content ?: "",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.textGray,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PostStat(icon = VisibilityOn, count = post.viewCount)
            PostStatDivider()
            PostStat(icon = HeartOutlined, count = post.likeCount)
            PostStatDivider()
            PostStat(icon = Comment, count = post.commentCount)
        }
    }
}

@Composable
private fun PostActionsRow(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
                    color = AniPickTheme.colors.textGray,
                )
            }
        }
        Icon(
            imageVector = Share,
            contentDescription = "공유",
            tint = AniPickTheme.colors.textGray,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onShareClick),
        )
    }
}
