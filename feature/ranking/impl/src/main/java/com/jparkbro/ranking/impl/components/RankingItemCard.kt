package com.jparkbro.ranking.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.jparkbro.core.designsystem.icon.RankingDown
import com.jparkbro.core.designsystem.icon.RankingHold
import com.jparkbro.core.designsystem.icon.RankingUp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RankingTrend

@Composable
internal fun RankingItemCard(
    anime: Anime,
    modifier: Modifier = Modifier,
    showRankChange: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .clickable { onClick(anime.animeId ?: 0) }
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            anime.rank?.let {
                Text(
                    text = it.toString().padStart(2, '0'),
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.black
                )
            }
            when (anime.trend) {
                RankingTrend.UP, RankingTrend.DOWN, RankingTrend.SAME -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (anime.trend) {
                                RankingTrend.UP -> RankingUp
                                RankingTrend.DOWN -> RankingDown
                                else -> RankingHold
                            },
                            contentDescription = "랭킹 변화 아이콘",
                            modifier = Modifier
                                .size(8.dp),
                            tint = when (anime.trend) {
                                RankingTrend.UP -> AniPickTheme.colors.point
                                RankingTrend.DOWN -> AniPickTheme.colors.primary
                                else -> AniPickTheme.colors.textGray
                            }
                        )
                        if (showRankChange) {
                            anime.change?.let {
                                Text(
                                    text = if (anime.change == "N") "New" else it,
                                    style = AniPickTheme.typography.caption1,
                                    color = when (anime.trend) {
                                        RankingTrend.UP -> AniPickTheme.colors.point
                                        RankingTrend.DOWN -> AniPickTheme.colors.primary
                                        else -> AniPickTheme.colors.textGray
                                    }
                                )
                            }
                        }
                    }
                }
                else -> {
                    if (showRankChange) {
                        Text(
                            text = "New",
                            style = AniPickTheme.typography.caption2,
                            color = AniPickTheme.colors.primary
                        )
                    }
                }
            }
        }
        AsyncImage(
            model = anime.coverImageUrl.takeIf { !it.contains("default.jpg") },
            contentDescription = "${anime.title} 커버 이미지",
            error = painterResource(R.drawable.anime_card_default_img),
            placeholder = painterResource(R.drawable.anime_card_default_img),
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(128f / 182f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = anime.title,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                anime.genres.forEach { genre ->
                    Text(
                        text = genre,
                        style = AniPickTheme.typography.caption2,
                        color = AniPickTheme.colors.primary,
                        modifier = Modifier
                            .background(AniPickTheme.colors.primary10, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
internal fun RankingItemCardSkeleton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AniPickShimmerBox(modifier = Modifier.width(20.dp).height(16.dp))
            AniPickShimmerBox(modifier = Modifier.width(16.dp).height(10.dp))
        }
        AniPickShimmerBox(
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(128f / 182f),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AniPickShimmerBox(modifier = Modifier.width(50.dp).height(24.dp))
                AniPickShimmerBox(modifier = Modifier.width(50.dp).height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingItemCardSkeletonPreview() {
    RankingItemCardSkeleton(contentPadding = PaddingValues(20.dp))
}

@Preview(showBackground = true)
@Composable
private fun RankingItemCardPreview() {
    RankingItemCard(
        Anime(
            animeId = 1L,
            title = "샘플 애니메이션",
            coverImageUrl = "",
            genres = listOf("액션", "판타지"),
            rank = 1,
            trend = RankingTrend.UP,
            change = "20"
        ),
        showRankChange = true,
        onClick = {}
    )
}
