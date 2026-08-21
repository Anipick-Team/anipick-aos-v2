package com.jparkbro.core.model.anime

data class Anime(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<String>? = null,
    val rank: Int? = null,
    val subtitle: String? = null,
    val clickLog: String? = null,
    val impressionLog: String? = null,
    val isAdult: Boolean? = null,
    val change: String? = null,
    val trend: RankingTrend? = null,
    val popularity: Int? = null,
    val trending: Int? = null,
    val userAnimeStatusId: Int? = null,
    val myRating: Float? = null,
    val animeLikeId: Int? = null,
    val seasonYear: String? = null,
)
