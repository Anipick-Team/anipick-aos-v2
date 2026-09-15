package com.jparkbro.catalog.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.catalog.impl.actor.CatalogActorRoot
import com.jparkbro.catalog.impl.anime.CatalogAnimeRoot
import com.jparkbro.catalog.impl.character.CatalogCharacterRoot
import com.jparkbro.catalog.impl.recommendation.CatalogRecommendationRoot
import com.jparkbro.catalog.impl.series.CatalogSeriesRoot
import com.jparkbro.catalog.impl.studio.CatalogStudioRoot
import com.jparkbro.community.api.CommunityNavKey
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.review.api.ReviewNavKey

/** [CatalogNavKey]의 각 키별 contentKey 접두어 - 실제 contentKey는 여기에 식별자(animeId 등)를 붙여서 만든다 */
const val CATALOG_ANIME_CONTENT_KEY = "CatalogNavKey.Anime"
const val CATALOG_ACTOR_CONTENT_KEY = "CatalogNavKey.Actor"
const val CATALOG_CHARACTER_CONTENT_KEY = "CatalogNavKey.Character"
const val CATALOG_STUDIO_CONTENT_KEY = "CatalogNavKey.Studio"
const val CATALOG_SERIES_CONTENT_KEY = "CatalogNavKey.Series"
const val CATALOG_RECOMMENDATION_CONTENT_KEY = "CatalogNavKey.Recommendation"

fun EntryProviderScope<NavKey>.catalogEntry(
    navigator: Navigator,
) {
    entry<CatalogNavKey.Anime>(clazzContentKey = { key -> "$CATALOG_ANIME_CONTENT_KEY-${key.animeId}" }) { key ->
        CatalogAnimeRoot(
            animeId = key.animeId,
            onBackClick = navigator::goBack,
            onNavigateToStudio = { studioId -> navigator.navigate(CatalogNavKey.Studio(studioId)) },
            onNavigateToCommunity = { board ->
                // CatalogAnimeViewModel.loadCommunityBoard가 hasBoard && seriesId != null일 때만 이 이벤트를 보낸다.
                board.seriesId?.let { seriesId ->
                    navigator.navigate(
                        CommunityNavKey.Main(
                            seriesId = seriesId,
                            title = board.title,
                            coverImageUrl = board.coverImageUrl,
                            genres = board.genres?.mapNotNull { it.name },
                        )
                    )
                }
            },
            onNavigateToReviewWrite = { animeId -> navigator.navigate(ReviewNavKey.Write(animeId)) },
            onNavigateToCharacterList = { animeId -> navigator.navigate(CatalogNavKey.Character(animeId)) },
            onNavigateToActorDetail = { personId -> navigator.navigate(CatalogNavKey.Actor(personId)) },
            onNavigateToSeries = { animeId, animeTitle -> navigator.navigate(CatalogNavKey.Series(animeId, animeTitle)) },
            onNavigateToRecommendation = { animeId -> navigator.navigate(CatalogNavKey.Recommendation(animeId)) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
    entry<CatalogNavKey.Actor>(clazzContentKey = { key -> "$CATALOG_ACTOR_CONTENT_KEY-${key.personId}" }) { key ->
        CatalogActorRoot(
            personId = key.personId,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
    entry<CatalogNavKey.Character>(clazzContentKey = { key -> "$CATALOG_CHARACTER_CONTENT_KEY-${key.animeId}" }) { key ->
        CatalogCharacterRoot(
            animeId = key.animeId,
            onBackClick = navigator::goBack,
            onNavigateToActorDetail = { personId -> navigator.navigate(CatalogNavKey.Actor(personId)) },
        )
    }
    entry<CatalogNavKey.Studio>(clazzContentKey = { key -> "$CATALOG_STUDIO_CONTENT_KEY-${key.studioId}" }) { key ->
        CatalogStudioRoot(
            studioId = key.studioId,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
    entry<CatalogNavKey.Series>(clazzContentKey = { key -> "$CATALOG_SERIES_CONTENT_KEY-${key.animeId}" }) { key ->
        CatalogSeriesRoot(
            animeId = key.animeId,
            animeTitle = key.animeTitle,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
    entry<CatalogNavKey.Recommendation>(clazzContentKey = { key -> "$CATALOG_RECOMMENDATION_CONTENT_KEY-${key.basedOnAnimeId}" }) { key ->
        CatalogRecommendationRoot(
            basedOnAnimeId = key.basedOnAnimeId,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
}
