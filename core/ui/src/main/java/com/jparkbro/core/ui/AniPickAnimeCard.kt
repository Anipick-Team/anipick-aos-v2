package com.jparkbro.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime

/**
 * 128x182, 114x162 두 사이즈로 쓰이는데 둘 다 가로/세로 비율이 사실상 같다(0.7033 / 0.7037).
 * 그래서 가로/세로를 각각 고정값으로 넣지 않고, 이 비율 하나만 [Modifier.aspectRatio]로 주고
 * 세로는 [cardWidth]에 맞춰 자동으로 계산되게 한다 — cardWidth=114.dp를 넣으면 세로가 약
 * 162.1dp로 나와서 114x162 쪽과도 그대로 맞는다.
 */
private const val ANIME_CARD_ASPECT_RATIO = 128f / 182f

@Composable
fun AniPickAnimeCard(
    anime: Anime,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 128.dp,
    maxLine: Int = 2,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AsyncImage(
                model = anime.coverImageUrl.ifBlank { null } ?: R.drawable.anime_card_default_img,
                contentDescription = anime.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ANIME_CARD_ASPECT_RATIO)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
            if (anime.rank != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 10.dp, start = 10.dp)
                        .size(36.dp)
                        .background(AniPickTheme.colors.primary, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${anime.rank}",
                        style = AniPickTheme.typography.body1,
                        color = AniPickTheme.colors.white
                    )
                }
            }
        }
        Text(
            text = anime.title,
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            minLines = maxLine,
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis,
        )
        anime.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickAnimeCardPreview() {
    Column(

    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AniPickAnimeCard(
                anime = Anime(title = "귀멸의 칼날: 무한열차편", coverImageUrl = "", rank = 1),
                cardWidth = 128.dp,
            )
            AniPickAnimeCard(
                anime = Anime(title = "귀멸의 칼날", coverImageUrl = "", rank = 1),
                cardWidth = 128.dp,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AniPickAnimeCard(
                anime = Anime(title = "귀멸의 칼날: 무한열차편", coverImageUrl = "", subtitle = "2025년 6월"),
                cardWidth = 114.dp,
            )
            AniPickAnimeCard(
                anime = Anime(title = "진격의 거인", coverImageUrl = ""),
                cardWidth = 114.dp,
            )
        }
    }
}
