package com.jparkbro.review.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.review.api.ReviewNavKey
import com.jparkbro.review.impl.rated.RatedReviewRoot
import com.jparkbro.review.impl.recent.RecentReviewRoot
import com.jparkbro.review.impl.write.ReviewWriteRoot

/** [ReviewNavKey]의 각 키별 contentKey */
const val REVIEW_RECENT_CONTENT_KEY = "ReviewNavKey.Recent"
const val REVIEW_WRITE_CONTENT_KEY = "ReviewNavKey.Write"
const val REVIEW_RATED_CONTENT_KEY = "ReviewNavKey.Rated"

fun EntryProviderScope<NavKey>.reviewEntry(
    navigator: Navigator,
) {
    entry<ReviewNavKey.Recent>(clazzContentKey = { REVIEW_RECENT_CONTENT_KEY }) {
        RecentReviewRoot(
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToWrite = { animeId -> navigator.navigate(ReviewNavKey.Write(animeId)) },
        )
    }
    entry<ReviewNavKey.Write>(clazzContentKey = { REVIEW_WRITE_CONTENT_KEY }) { key ->
        ReviewWriteRoot(
            animeId = key.animeId,
            onBackClick = navigator::goBack,
            onSubmitSuccess = navigator::goBack,
        )
    }
    entry<ReviewNavKey.Rated>(clazzContentKey = { REVIEW_RATED_CONTENT_KEY }) {
        RatedReviewRoot(
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToWrite = { animeId -> navigator.navigate(ReviewNavKey.Write(animeId)) },
        )
    }
}
