package com.jparkbro.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.search.api.SearchNavKey
import com.jparkbro.search.impl.detail.SearchDetailRoot
import com.jparkbro.search.impl.main.SearchMainRoot
import kr.agromarket.at.core.navigation.Navigator

const val SEARCH_MAIN_CONTENT_KEY = "SearchNavKey.Main"
const val SEARCH_DETAIL_CONTENT_KEY = "SearchNavKey.Detail"

fun EntryProviderScope<NavKey>.searchEntry(
    navigator: Navigator,
) {
    entry<SearchNavKey.Main>(clazzContentKey = { SEARCH_MAIN_CONTENT_KEY }) {
        SearchMainRoot(
            onBackClick = navigator::goBack,
            onNavigateToDetail = { query -> navigator.navigate(SearchNavKey.Detail(query)) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
    entry<SearchNavKey.Detail>(clazzContentKey = { SEARCH_DETAIL_CONTENT_KEY }) { key ->
        SearchDetailRoot(
            query = key.query,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToActorDetail = { personId -> navigator.navigate(CatalogNavKey.Actor(personId)) },
            onNavigateToStudioDetail = { studioId -> navigator.navigate(CatalogNavKey.Studio(studioId)) },
        )
    }
}
