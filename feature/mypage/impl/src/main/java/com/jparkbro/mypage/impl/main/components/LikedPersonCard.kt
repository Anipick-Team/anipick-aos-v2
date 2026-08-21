package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.ui.util.orNullIfDefaultCover

private val LIKED_PERSON_CARD_WIDTH = 80.dp

@Composable
internal fun LikedPersonCard(
    actor: Actor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(LIKED_PERSON_CARD_WIDTH)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = actor.profileImage.orNullIfDefaultCover(),
            contentDescription = actor.name,
            error = painterResource(R.drawable.profile_default_img),
            placeholder = painterResource(R.drawable.profile_default_img),
            modifier = Modifier
                .size(LIKED_PERSON_CARD_WIDTH)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = actor.name ?: "-",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LikedPersonCardPreview() {
    LikedPersonCard(
        actor = Actor(personId = 1, name = "카지 유우키", profileImage = ""),
    )
}
