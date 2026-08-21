package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.mypage.WatchCounts
import kotlinx.serialization.Serializable

@Serializable
data class MyPageResponse(
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val watchCounts: WatchCountsResponse? = null,
    val likedAnimes: List<LikedAnimeResponse>? = null,
    val likedPersons: List<LikedPersonResponse>? = null,
)

@Serializable
data class WatchCountsResponse(
    val watchList: Int? = null,
    val watching: Int? = null,
    val finished: Int? = null,
)

@Serializable
data class LikedAnimeResponse(
    val animeId: Long? = null,
    val animeLikeId: Int? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

@Serializable
data class LikedPersonResponse(
    val personId: Long,
    val userLikedVoiceActorId: Int? = null,
    val name: String? = null,
    val profileImageUrl: String? = null,
)

fun MyPageResponse.toMyPageProfile(): MyPageProfile = MyPageProfile(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    watchCounts = watchCounts?.let {
        WatchCounts(watchList = it.watchList, watching = it.watching, finished = it.finished)
    },
    likedAnimes = likedAnimes?.map { it.toAnime() },
    likedPersons = likedPersons?.map { it.toActor() },
)

fun LikedAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
    animeLikeId = animeLikeId,
)

fun LikedPersonResponse.toActor(): Actor = Actor(
    personId = personId,
    name = name,
    profileImage = profileImageUrl,
    userLikedVoiceActorId = userLikedVoiceActorId,
)
