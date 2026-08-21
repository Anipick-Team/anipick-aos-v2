package com.jparkbro.core.ui.component

import com.jparkbro.core.model.metadata.FilterType

enum class AnimeFilterTab(val label: String) {
    YEAR_SEASON("년도/분기"),
    GENRE("장르"),
    TYPE("타입"),
}

fun FilterType.toAnimeFilterTab(): AnimeFilterTab = when (this) {
    FilterType.YEAR, FilterType.SEASON -> AnimeFilterTab.YEAR_SEASON
    FilterType.GENRE -> AnimeFilterTab.GENRE
    FilterType.TYPE -> AnimeFilterTab.TYPE
}
