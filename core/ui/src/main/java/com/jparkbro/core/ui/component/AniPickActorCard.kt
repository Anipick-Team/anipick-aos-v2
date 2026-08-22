package com.jparkbro.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor

@Composable
fun AniPickActorCard(
    actor: Actor,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 128.dp,
    maxLine: Int = 2,
    background: AniPickCardBackground = AniPickCardBackground.GRAY,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AniPickAnimeCoverImage(
            coverImageUrl = actor.profileImage,
            contentDescription = actor.name,
            modifier = Modifier.fillMaxWidth(),
            background = background,
        )
        Text(
            text = actor.name ?: "-",
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            minLines = maxLine,
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickActorCardPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AniPickActorCard(
            actor = Actor(personId = 1L, name = "카지 유우키", profileImage = ""),
            cardWidth = 114.dp,
        )
        AniPickActorCard(
            actor = Actor(personId = 2L, name = "이시카와 유이", profileImage = ""),
            cardWidth = 114.dp,
            background = AniPickCardBackground.GRAY,
        )
    }
}
