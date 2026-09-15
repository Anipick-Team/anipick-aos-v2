package com.jparkbro.core.ui.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickAnimatedChevronIcon
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.component.AniPickDropdownMenuIcon
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.component.AniPickStarRatingBar
import com.jparkbro.core.designsystem.icon.MoreVertical
import com.jparkbro.core.designsystem.model.AniPickDropdownMenuItem
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 리뷰 카드 - [showAnimeHeader]는 커버/제목 노출 여부, [showProfile]은 프로필/닉네임 노출 여부,
 *  [ratingAlignedToStart]는 별점 Row를 왼쪽에 붙일지 여부 */
@Composable
fun AniPickReviewCard(
    review: Review,
    modifier: Modifier = Modifier,
    showAnimeHeader: Boolean = true,
    showProfile: Boolean = true,
    ratingAlignedToStart: Boolean = false,
    onAnimeClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    onBlockClick: () -> Unit = {},
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isOverflowing by remember(review.content) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showAnimeHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAnimeClick),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = review.animeCoverImageUrl.orNullIfDefaultCover(),
                    contentDescription = "${review.animeTitle} 커버 이미지",
                    error = painterResource(R.drawable.review_card_default_img),
                    placeholder = painterResource(R.drawable.review_card_default_img),
                    modifier = Modifier.size(width = 116.dp, height = 108.dp),
                    contentScale = ContentScale.Crop,
                )
                review.animeTitle?.let {
                    Text(
                        text = it,
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.black,
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = when {
                    showProfile -> Arrangement.SpaceBetween
                    ratingAlignedToStart -> Arrangement.Start
                    else -> Arrangement.End
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showProfile) {
                    AniPickProfileNickname(
                        profileImageUrl = review.profileImageUrl,
                        profileImageBytes = review.profileImageBytes,
                        nickname = review.nickname,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AniPickStarRatingBar(
                        rating = review.rating ?: 0f,
                        onRatingChange = {},
                        enabled = false,
                        starSize = 20.dp,
                        spacing = 0.dp,
                    )
                    Text(
                        text = "${review.rating}",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.point,
                    )
                }
            }
            review.createdAt?.let {
                Text(
                    text = it.replace("-", "."),
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        }
        review.content?.let { content ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = content,
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.black,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { result ->
                        if (!isExpanded) isOverflowing = result.hasVisualOverflow
                    },
                )
                if (isOverflowing) {
                    Row(
                        modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded }),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (isExpanded) "접기" else "더보기",
                            style = AniPickTheme.typography.body1,
                            color = AniPickTheme.colors.primary,
                        )
                        AniPickAnimatedChevronIcon(
                            isExpanded = isExpanded,
                            contentDescription = if (isExpanded) "접기 아이콘" else "더보기 아이콘",
                            modifier = Modifier.size(16.dp),
                            tint = AniPickTheme.colors.primary,
                        )
                    }
                }
            }
        }
        if (!review.content.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isLiked = review.isLiked == true
                    AniPickAnimatedHeartIcon(
                        isLiked = isLiked,
                        onClick = onLikeClick,
                        contentDescription = if (isLiked) "좋아요 취소" else "좋아요",
                        modifier = Modifier.size(16.dp),
                        unlikedTint = AniPickTheme.colors.textGray,
                    )
                    Text(
                        text = "${review.likeCount ?: 0}",
                        style = AniPickTheme.typography.caption1,
                        color = if (isLiked) AniPickTheme.colors.point else AniPickTheme.colors.textGray
                    )
                }
                AniPickDropdownMenuIcon(
                    items = if (review.isMine == true) {
                        listOf(
                            reviewMenuItem("수정", AniPickTheme.colors.black, onEditClick),
                            reviewMenuItem("삭제", AniPickTheme.colors.black, onDeleteClick),
                        )
                    } else {
                        listOf(
                            reviewMenuItem("신고", AniPickTheme.colors.black, onReportClick),
                            reviewMenuItem("차단", AniPickTheme.colors.black, onBlockClick),
                        )
                    },
                    trigger = {
                        Icon(
                            imageVector = MoreVertical,
                            contentDescription = "리뷰 메뉴 더보기 아이콘",
                            tint = AniPickTheme.colors.textGray,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                )
            }
        }
    }
}

private fun reviewMenuItem(
    label: String,
    color: Color,
    onClick: () -> Unit,
) = AniPickDropdownMenuItem(
    content = {
        Text(
            text = label,
            style = AniPickTheme.typography.caption1,
            color = color,
        )
    },
    onClick = onClick,
)

@Composable
fun AniPickReviewCardSkeleton(
    modifier: Modifier = Modifier,
    showAnimeHeader: Boolean = true,
    showProfile: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (showAnimeHeader) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.size(width = 116.dp, height = 108.dp))
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp))
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (showProfile) Arrangement.SpaceBetween else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (showProfile) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AniPickShimmerBox(modifier = Modifier.size(30.dp), shape = CircleShape)
                        AniPickShimmerBox(modifier = Modifier.width(60.dp).height(14.dp))
                    }
                }
                AniPickShimmerBox(modifier = Modifier.width(100.dp).height(20.dp))
            }
            AniPickShimmerBox(modifier = Modifier.width(60.dp).height(12.dp))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.size(16.dp))
                AniPickShimmerBox(modifier = Modifier.width(20.dp).height(12.dp))
            }
            AniPickShimmerBox(modifier = Modifier.size(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickReviewCardPreview() {
    AniPickReviewCard(review = Review(
        reviewId = 1L,
        userId = 1L,
        animeId = 1L,
        animeTitle = "샘플 애니메이션",
        animeCoverImageUrl = "",
        content = "이 작품은 정말 인상 깊었습니다. 스토리 전개와 캐릭터 성장이 훌륭했어요.",
        nickname = "닉네임",
        profileImageUrl = "",
        createdAt = "2026-08-01",
        rating = 4.5f,
        likeCount = 12,
        isLiked = false,
        isMine = false,
        isSpoiler = false,
    ))
}

@Preview(showBackground = true)
@Composable
private fun AniPickReviewCardNoProfilePreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickReviewCard(
            review = Review(
                reviewId = 1L,
                animeId = 1L,
                animeTitle = "샘플 애니메이션",
                animeCoverImageUrl = "",
                content = "이 작품은 정말 인상 깊었습니다.",
                createdAt = "2026-08-01",
                rating = 4.5f,
                likeCount = 12,
                isMine = true,
            ),
            showProfile = false,
        )
        AniPickReviewCard(
            review = Review(
                reviewId = 1L,
                animeId = 1L,
                animeTitle = "샘플 애니메이션",
                animeCoverImageUrl = "",
                createdAt = "2026-08-01",
                rating = 4.5f,
                isMine = true,
            ),
            showProfile = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickReviewCardNoAnimeHeaderPreview() {
    AniPickReviewCard(
        review = Review(
            reviewId = 1L,
            content = "이 작품은 정말 인상 깊었습니다.",
            nickname = "닉네임",
            profileImageUrl = "",
            createdAt = "2026-08-01",
            rating = 4.5f,
            likeCount = 12,
        ),
        showAnimeHeader = false,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickReviewCardSkeletonPreview() {
    AniPickReviewCardSkeleton()
}
