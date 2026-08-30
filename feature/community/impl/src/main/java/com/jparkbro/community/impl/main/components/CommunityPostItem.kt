package com.jparkbro.community.impl.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.community.impl.components.PostStat
import com.jparkbro.community.impl.components.PostStatDivider
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.icon.Comment
import com.jparkbro.core.designsystem.icon.HeartOutlined
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.ui.component.AniPickProfileNickname

/** 게시글 목록 한 줄 - 작성자/작성일 + 제목·본문 미리보기 + 통계 + 스포일러 여부 */
@Composable
internal fun CommunityPostItem(
    post: CommunityPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AniPickProfileNickname(profileImageUrl = post.profileImageUrl, nickname = post.nickname)
            post.createdAt?.let { createdAt ->
                Text(
                    text = createdAt,
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = post.title ?: "-",
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = post.content ?: "",
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.textGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            post.thumbnailImageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "커뮤니티 게시글 썸네일 이미지",
                    error = painterResource(R.drawable.portrait_default_img),
                    placeholder = painterResource(R.drawable.portrait_default_img),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(112.dp),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
            if (post.isSpoiler == true) {
                AniPickGenreTag(genre = "스포일러")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityPostItemPreview() {
    CommunityPostItem(
        post = CommunityPost(
            postId = 1L,
            nickname = "anipick_x2k3",
            title = "이번 화 진짜 미쳤다",
            content = "이번 화 전개 보고 소름 돋았음... 다들 봤어?",
            isSpoiler = true,
            viewCount = 102,
            likeCount = 32,
            commentCount = 8,
            createdAt = "2025. 04. 03",
            thumbnailImageUrl = ""
        ),
        onClick = {},
    )
}
