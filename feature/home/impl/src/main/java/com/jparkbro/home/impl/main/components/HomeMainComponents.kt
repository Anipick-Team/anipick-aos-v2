package com.jparkbro.home.impl.main.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.ui.component.AniPickAnimeCard
import com.jparkbro.core.ui.util.objectParticleFor

/** "오늘의 추천작" 섹션 제목
 *  [referenceAnimeTitle] 있음: 그 작품 기준 문구, 없음: [nickname] 기준 문구 */
@Composable
internal fun RecommendationSectionTitle(
    nickname: String?,
    referenceAnimeTitle: String?,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = AniPickTheme.typography.h3,
    accentColor: Color = AniPickTheme.colors.primary,
    baseColor: Color = AniPickTheme.colors.black,
) {
    Column(modifier = modifier) {
        if (referenceAnimeTitle != null) {
            Row {
                Text(
                    text = referenceAnimeTitle,
                    style = titleStyle,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = "${objectParticleFor(referenceAnimeTitle)} 재밌게 보셨다면,",
                    style = titleStyle,
                    color = baseColor,
                )
            }
            Text(
                text = "이 작품들도 마음에 드실 거에요!",
                style = titleStyle,
                color = baseColor,
            )
        } else {
            Row {
                Text(
                    text = "오늘의 추천작, ",
                    style = titleStyle,
                    color = baseColor,
                )
                Text(
                    text = nickname.orEmpty(),
                    style = titleStyle,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = " 님의",
                    style = titleStyle,
                    color = baseColor,
                )
            }
            Text(
                text = "취향에 맞춰 준비했어요!",
                style = titleStyle,
                color = baseColor,
            )
        }
    }
}

/** "최근 찾아보신 OO과 비슷한 작품이에요!" 섹션 제목. [referenceAnimeTitle]은 기준이 된 애니 제목 */
@Composable
internal fun SimilarRecommendationSectionTitle(
    referenceAnimeTitle: String?,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = AniPickTheme.typography.h3,
    accentColor: Color = AniPickTheme.colors.primary,
    baseColor: Color = AniPickTheme.colors.black,
) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = "최근 찾아보신 ",
                style = titleStyle,
                color = baseColor,
            )
            Text(
                text = referenceAnimeTitle.orEmpty(),
                style = titleStyle,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            Text(
                text = " 과",
                style = titleStyle,
                color = baseColor,
            )
        }
        Text(
            text = "비슷한 작품이에요!",
            style = titleStyle,
            color = baseColor,
        )
    }
}

/** 홈 화면 애니 목록 섹션들이 공통으로 쓰는 가로 스크롤 카드 목록. */
@Composable
internal fun AnimeCardRow(
    animes: List<Anime>,
    onAnimeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(animes) { anime ->
            AniPickAnimeCard(
                anime = anime,
                onClick = { anime.animeId?.let(onAnimeClick) },
            )
        }
    }
}

internal const val RECOMMENDATION_EMPTY_MESSAGE = "아직 추천할 콘텐츠가 없어요.\n먼저 평가를 진행해주세요."

internal const val WEEKLY_EMPTY_MESSAGE = "이 요일에 방영하는 신작이 없어요.\n다른 요일을 확인해 보세요!"

internal const val SIMILAR_EMPTY_MESSAGE = "아직 찾아본 애니가 없어요.\n다양한 애니를 탐색해 보세요!"

/** 안내 배너의 배경과 마스코트 한 세트 - 색과 높이가 서로 맞춰져 있어 섞어 쓰지 않는다. */
internal enum class EmptyBannerStyle(
    @DrawableRes val backgroundRes: Int,
    @DrawableRes val imageRes: Int,
) {
    Type1(R.drawable.recommendation_empty_bg, R.drawable.recommendation_empty_img),
    Type2(R.drawable.recommendation_empty_bg2, R.drawable.recommendation_empty_img2),
}

/**
 * 목록이 비었을 때 [HomeSection]째로 대신 넣는 안내 배너
 * 높이는 [style] 배경의 원래 높이를 따르고, 너비가 좁으면 배경 좌우가 잘린다.
 */
@Composable
internal fun EmptyRecommendationBanner(
    message: String,
    style: EmptyBannerStyle,
    modifier: Modifier = Modifier,
) {
    val backgroundRes = style.backgroundRes
    val imageRes = style.imageRes
    val background = painterResource(backgroundRes)
    val backgroundHeight = with(LocalDensity.current) { background.intrinsicSize.height.toDp() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(backgroundHeight)
            .clip(RoundedCornerShape(8.dp)),
    ) {
        Image(
            painter = background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.white,
                modifier = Modifier.weight(1f),
            )
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
internal fun RecentReviewCard(
    review: Review,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(228.dp)
            .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
            .border(1.dp, AniPickTheme.colors.gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        review.animeTitle?.let {
            Text(
                text = it,
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        review.content?.let {
            Text(
                text = it,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            review.nickname?.let {
                Text(
                    text = it,
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
            }
            VerticalDivider(
                modifier = Modifier.height(10.dp),
                thickness = 1.dp,
                color = AniPickTheme.colors.textGray
            )
            review.createdAt?.let {
                Text(
                    text = it,
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyRecommendationBannerType1Preview() {
    EmptyRecommendationBanner(
        message = RECOMMENDATION_EMPTY_MESSAGE,
        style = EmptyBannerStyle.Type1,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
@Preview(showBackground = true)
private fun EmptyRecommendationBannerType2Preview() {
    EmptyRecommendationBanner(
        message = SIMILAR_EMPTY_MESSAGE,
        style = EmptyBannerStyle.Type2,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
@Preview(showBackground = true, widthDp = 320)
private fun EmptyRecommendationBannerNarrowPreview() {
    EmptyRecommendationBanner(
        message = RECOMMENDATION_EMPTY_MESSAGE,
        style = EmptyBannerStyle.Type1,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
@Preview(showBackground = true, widthDp = 600)
private fun EmptyRecommendationBannerWidePreview() {
    EmptyRecommendationBanner(
        message = RECOMMENDATION_EMPTY_MESSAGE,
        style = EmptyBannerStyle.Type1,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
@Preview(showBackground = true)
private fun RecentReviewCardPreview() {
    RecentReviewCard(
        review = Review(
            animeTitle = "title",
            content = "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent",
            nickname = "nicknamenicknamenicknamenicknamenicknamenicknamenicknamenickname",
            createdAt = "2026.08.01",
        ),
    )
}
