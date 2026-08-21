package com.jparkbro.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickShimmerBox

/** [AniPickAnimeCard]와 같은 비율([CARD_ASPECT_RATIO])로 로딩 중 자리를 채운다. */
@Composable
fun AniPickAnimeCardSkeleton(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 128.dp,
) {
    Column(
        modifier = modifier.width(cardWidth),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AniPickShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(CARD_ASPECT_RATIO),
        )
        AniPickShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(14.dp),
        )
    }
}
