package com.jparkbro.core.ui

/** [AniPickAnimeFilterBottomSheet]의 탭 — 년도/분기는 하나로 묶여서 탭 하나에 같이 보인다. */
enum class AnimeFilterTab(val label: String) {
    YEAR_SEASON("년도/분기"),
    GENRE("장르"),
    TYPE("타입"),
}
