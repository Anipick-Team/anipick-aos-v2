package com.jparkbro.catalog.impl.recommendation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.jparkbro.catalog.impl.components.CatalogBannerHeader
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.util.withParticleFor

/** "'OOO'와 함께 보기 좋은 작품" 헤더. [CatalogBannerHeader]로 Home Detail 추천 헤더와 동일한 배너를 쓴다. */
@Composable
internal fun CatalogRecommendationHeader(
    referenceAnimeTitle: String?,
) {
    CatalogBannerHeader {
        val title = referenceAnimeTitle.orEmpty()
        Row {
            Text(
                text = "'$title'",
                style = AniPickTheme.typography.h2,
                color = AniPickTheme.colors.white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            Text(
                text = "${withParticleFor(title)} 함께",
                style = AniPickTheme.typography.h2,
                color = AniPickTheme.colors.white,
            )
        }
        Text(
            text = "보기 좋은 작품",
            style = AniPickTheme.typography.h2,
            color = AniPickTheme.colors.white,
        )
    }
}
