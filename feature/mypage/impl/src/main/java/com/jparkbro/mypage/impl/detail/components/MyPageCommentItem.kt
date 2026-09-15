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
import com.jparkbro.core.designsystem.component.AniPickPostStat
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.HeartOutlined
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 마이페이지 "내 댓글" 목록 한 건 - 원글 제목을 헤더로, 내가 쓴 댓글 내용을 본문으로 보여준다. */
@Composable
internal fun MyPageCommentItem(
    comment: MyCommunityComment,
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
                model = comment.animeCoverImageUrl.orNullIfDefaultCover(),
                contentDescription = "${comment.animeTitle} 커버 이미지",
                error = painterResource(R.drawable.review_card_default_img),
                placeholder = painterResource(R.drawable.review_card_default_img),
                modifier = Modifier.size(width = 116.dp, height = 108.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = comment.animeTitle ?: "-",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        comment.createdAt?.let {
            Text(
                text = it.replace("-", "."),
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
        }
        Text(
            text = comment.postTitle ?: "-",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.textGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = comment.content ?: "",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        AniPickPostStat(icon = HeartOutlined, count = comment.likeCount)
    }
}

@Composable
internal fun MyPageCommentItemSkeleton(modifier: Modifier = Modifier) {
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
        AniPickShimmerBox(modifier = Modifier.width(80.dp).height(12.dp))
        AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(16.dp))
        AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
        AniPickShimmerBox(modifier = Modifier.width(30.dp).height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageCommentItemPreview() {
    MyPageCommentItem(
        comment = MyCommunityComment(
            commentId = 1L,
            postId = 1L,
            animeTitle = "샘플 애니메이션",
            animeCoverImageUrl = "",
            postTitle = "이번 화 진짜 미쳤다",
            content = "저도 이번 화 보고 소름 돋았어요 ㅋㅋ",
            likeCount = 4,
            createdAt = "2025-04-03",
        ),
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MyPageCommentItemSkeletonPreview() {
    MyPageCommentItemSkeleton()
}
