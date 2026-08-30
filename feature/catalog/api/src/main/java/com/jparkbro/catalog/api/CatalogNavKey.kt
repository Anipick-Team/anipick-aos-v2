package com.jparkbro.catalog.api

import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.navigation.Navigator
import kotlinx.serialization.Serializable

sealed interface CatalogNavKey : NavKey {

    @Serializable
    data class Anime(val animeId: Long) : CatalogNavKey

    @Serializable
    data class Actor(val personId: Long) : CatalogNavKey

    /** 애니 등장인물/성우 목록 - [animeId] 기준으로 조회한다. */
    @Serializable
    data class Character(val animeId: Long) : CatalogNavKey

    @Serializable
    data class Studio(val studioId: Long) : CatalogNavKey

    /** 시리즈(관련 작품) 전체보기 - [animeId] 기준으로 조회한다. [animeTitle]은 헤더 문구용으로,
     *  시리즈 조회 API 응답에는 포함되지 않아 진입 시점(애니 상세)에서 그대로 넘겨받는다. */
    @Serializable
    data class Series(val animeId: Long, val animeTitle: String) : CatalogNavKey

    /** 특정 애니 기반 "이 작품과 비슷한 작품" 추천 전체보기 - [HomeDetailType.Recommendation]과 동일하게
     *  [basedOnAnimeId] 기준으로 조회한다. */
    @Serializable
    data class Recommendation(val basedOnAnimeId: Long) : CatalogNavKey
}