package com.jparkbro.auth.impl.preferencesetup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.component.AniPickStarRatingBar
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime

@Composable
internal fun PreferenceSetupAnimeItem(
    anime: Anime,
    committedRating: Float,
    onSaveRating: (Float) -> Unit,
    onCancelRating: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var draftRating by rememberSaveable { mutableFloatStateOf(committedRating) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
                .clickable(onClick = { isExpanded = !isExpanded })
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.coverImageUrl?.ifBlank { null } ?: R.drawable.default_image_preference,
                contentDescription = "애니메이션 커버 이미지",
                modifier = Modifier
                    .size(width = 132.dp, height = 88.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = anime.title ?: "-",
                        color = AniPickTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = AniPickTheme.typography.body2,
                        modifier = Modifier
                            .weight(1f)
                    )
                    if (committedRating != 0f) {
                        PreferenceSetupCompactButton(
                            onClick = { onCancelRating() },
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    maxLines = 1,
                ) {
                    anime.genres?.forEach { genre ->
                        AniPickGenreTag(genre = genre)
                    }
                }
                if (committedRating != 0f) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AniPickStarRatingBar(
                            rating = committedRating,
                            onRatingChange = {},
                            enabled = false,
                            starSize = 20.dp,
                            spacing = 0.dp,
                        )
                        Text(
                            text = "($committedRating)",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AniPickStarRatingBar(
                        rating = draftRating,
                        onRatingChange = { draftRating = it },
                        starSize = 28.dp,
                        spacing = 0.dp,
                    )
                    Text(
                        text = "($draftRating/5.0)",
                        style = AniPickTheme.typography.body1,
                        color = AniPickTheme.colors.point,
                    )
                }
                AniPickButton(
                    text = "평가하기",
                    onClick = {
                        onSaveRating(draftRating)
                        isExpanded = false
                    },
                    size = ButtonSize.S,
                )
            }
        }
    }
}

@Composable
internal fun PreferenceSetupAnimeItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AniPickShimmerBox(
            modifier = Modifier.size(width = 132.dp, height = 88.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AniPickShimmerBox(
                modifier = Modifier
                    .width(160.dp)
                    .height(18.dp)
            )
            AniPickShimmerBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(14.dp)
            )
        }
    }
}

@Composable
private fun PreferenceSetupCompactButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.primary)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "평가취소",
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferenceSetupAnimeItemPreview() {
    Column(
        modifier = Modifier
            .background(AniPickTheme.colors.white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PreferenceSetupAnimeItem(
            anime = Anime(
                animeId = 1L,
                title = "귀멸의 칼날 asdfdafdsfdsfdsafdsafdsfads",
                coverImageUrl = "",
                genres = listOf("액션", "판타지"),
            ),
            committedRating = 0f,
            onSaveRating = {},
            onCancelRating = {},
        )
        PreferenceSetupAnimeItem(
            anime = Anime(
                animeId = 2L,
                title = "진격의 거인",
                coverImageUrl = "",
                genres = listOf("액션", "드라마", "액션", "드라마", "다크판타지"),
            ),
            committedRating = 4.5f,
            onSaveRating = {},
            onCancelRating = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferenceSetupAnimeItemSkeletonPreview() {
    Column(
        modifier = Modifier
            .background(AniPickTheme.colors.white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PreferenceSetupAnimeItemSkeleton()
    }
}
