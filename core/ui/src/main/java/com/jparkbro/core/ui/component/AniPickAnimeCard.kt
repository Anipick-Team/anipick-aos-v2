package com.jparkbro.core.ui.component

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 애니/캐릭터 카드 커버 이미지 가로세로 비율 */
const val CARD_ASPECT_RATIO = 128f / 182f

/** 애니/캐릭터 카드 커버 이미지 - 없거나 플레이스홀더면 [background]에 맞는 기본 이미지로 대체 */
@Composable
fun AniPickAnimeCoverImage(
    coverImageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    background: AniPickCardBackground = AniPickCardBackground.WHITE,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    val defaultImageRes = when (background) {
        AniPickCardBackground.WHITE -> R.drawable.card_default_img_white
        AniPickCardBackground.GRAY -> R.drawable.card_default_img_gray
    }

    AsyncImage(
        model = coverImageUrl.orNullIfDefaultCover(),
        contentDescription = contentDescription,
        error = painterResource(defaultImageRes),
        placeholder = painterResource(defaultImageRes),
        modifier = modifier
            .aspectRatio(CARD_ASPECT_RATIO)
            .clip(shape),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun AniPickAnimeCard(
    anime: Anime,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 128.dp,
    maxLine: Int = 2,
    background: AniPickCardBackground = AniPickCardBackground.WHITE,
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
            AniPickAnimeCoverImage(
                coverImageUrl = anime.coverImageUrl,
                contentDescription = "${anime.title ?: "-"} 커버 이미지",
                modifier = Modifier.fillMaxWidth(),
                background = background,
            )
            anime.rank?.let { rank ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 10.dp, start = 10.dp)
                        .size(36.dp)
                        .background(AniPickTheme.colors.primary, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$rank",
                        style = AniPickTheme.typography.body1,
                        color = AniPickTheme.colors.white
                    )
                }
            }
        }
        Text(
            text = anime.title ?: "-",
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickTheme.colors.backgroundGray)
                .padding(12.dp),
        ) {
            AniPickAnimeCard(
                anime = Anime(title = "귀멸의 칼날: 무한열차편", coverImageUrl = ""),
                cardWidth = 114.dp,
                background = AniPickCardBackground.GRAY,
            )
        }
    }
}
