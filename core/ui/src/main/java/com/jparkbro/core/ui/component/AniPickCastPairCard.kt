package com.jparkbro.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.character.Character

/** 캐릭터+성우 페어 카드 */
@Composable
fun AniPickCastPairCard(
    animeCharacter: AnimeCharacter,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 100.dp,
    maxLine: Int = 2,
    background: AniPickCardBackground = AniPickCardBackground.WHITE,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.lightGray)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Column(modifier = Modifier.width(cardWidth)) {
            AniPickAnimeCoverImage(
                coverImageUrl = animeCharacter.character.imageUrl,
                contentDescription = "${animeCharacter.character.name ?: "-"} 캐릭터 이미지",
                modifier = Modifier.width(cardWidth),
                background = background,
                shape = RoundedCornerShape(topStart = 8.dp),
            )
            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = animeCharacter.character.name ?: "-",
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = maxLine,
                    minLines = maxLine,
                )
            }
        }
        animeCharacter.voiceActor?.let { voiceActor ->
            Column(modifier = Modifier.width(cardWidth)) {
                AniPickAnimeCoverImage(
                    coverImageUrl = voiceActor.profileImage,
                    contentDescription = "${voiceActor.name ?: "-"} 성우 이미지",
                    modifier = Modifier.width(cardWidth),
                    background = background,
                    shape = RoundedCornerShape(topEnd = 8.dp),
                )
                Box(
                    modifier = Modifier
                        .width(cardWidth)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = voiceActor.name ?: "-",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = maxLine,
                        minLines = maxLine,
                    )
                }
            }
        }
    }
}

/** [AniPickCastPairCard]와 같은 규격(캐릭터+성우 2칸)으로 로딩 중 자리를 채운다. */
@Composable
fun AniPickCastPairCardSkeleton(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 100.dp,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.lightGray),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(2) {
            Column(modifier = Modifier.width(cardWidth)) {
                AniPickShimmerBox(
                    modifier = Modifier
                        .width(cardWidth)
                        .aspectRatio(CARD_ASPECT_RATIO),
                )
                Box(
                    modifier = Modifier
                        .width(cardWidth)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    AniPickShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(16.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickCastPairCardPreview() {
    AniPickCastPairCard(
        animeCharacter = AnimeCharacter(
            character = Character(characterId = 1L, name = "엘런 예거", imageUrl = ""),
            voiceActor = Actor(personId = 1L, name = "카지 유우키", profileImage = ""),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickCastPairCardNoVoiceActorPreview() {
    AniPickCastPairCard(
        animeCharacter = AnimeCharacter(
            character = Character(characterId = 2L, name = "미카사 아커만", imageUrl = ""),
            voiceActor = null,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickCastPairCardSkeletonPreview() {
    AniPickCastPairCardSkeleton()
}
