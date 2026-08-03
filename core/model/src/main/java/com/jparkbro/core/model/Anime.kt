package com.jparkbro.core.model

data class Anime(
    val animeId: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val genres: List<String> = emptyList(),
    val rank: Int? = null,
    val releaseDate: String? = null,
    val clickLog: String? = null,
    val impressionLog: String? = null,
    val isAdult: Boolean = false,
    val change: String? = null,
    val trend: RankingTrend? = null,
    val popularity: Int? = null,
    val trending: Int? = null,
    val airDate: String? = null,
    val userAnimeStatusId: Int? = null,
    val myRating: Float? = null,
    val animeLikeId: Int? = null,
    val seasonYear: String = "",
)