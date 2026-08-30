package com.jparkbro.mypage.impl.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 마이페이지 "내 게시글" 목록 한 건 - 항상 내 글이라 프로필/닉네임은 표시하지 않는다. */
@Composable
internal fun MyPagePostItem(
    post: CommunityPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = post.animeCoverImageUrl.orNullIfDefaultCover(),
                contentDescription = "${post.title} 커버 이미지",
                error = painterResource(R.drawable.review_card_default_img),
                placeholder = painterResource(R.drawable.review_card_default_img),
                modifier = Modifier.size(width = 116.dp, height = 108.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = post.title ?: "-",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = post.content ?: "",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "조회 ${post.viewCount ?: 0} · 좋아요 ${post.likeCount ?: 0} · 댓글 ${post.commentCount ?: 0}",
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.textGray,
            )
            post.createdAt?.let {
                Text(
                    text = it.replace("-", "."),
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
            }
        }
    }
}

@Composable
internal fun MyPagePostItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AniPickShimmerBox(modifier = Modifier.size(width = 116.dp, height = 108.dp))
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp))
        }
        AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AniPickShimmerBox(modifier = Modifier.width(120.dp).height(12.dp))
            AniPickShimmerBox(modifier = Modifier.width(60.dp).height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePostItemPreview() {
    MyPagePostItem(
        post = CommunityPost(
            postId = 1L,
            animeCoverImageUrl = "",
            title = "이번 화 진짜 미쳤다",
            content = "이번 화 전개 보고 소름 돋았음... 다들 봤어?",
            viewCount = 102,
            likeCount = 32,
            commentCount = 8,
            createdAt = "2025-04-03",
        ),
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MyPagePostItemSkeletonPreview() {
    MyPagePostItemSkeleton()
}
