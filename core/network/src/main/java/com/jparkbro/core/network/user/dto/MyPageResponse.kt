package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.mypage.WatchCounts
import kotlinx.serialization.Serializable

@Serializable
data class MyPageResponse(
    val nickname: String,
    val profileImageUrl: String? = null,
    val watchCounts: WatchCountsResponse = WatchCountsResponse(),
    val likedAnimes: List<LikedAnimeResponse> = emptyList(),
    val likedPersons: List<LikedPersonResponse> = emptyList(),
)

@Serializable
data class WatchCountsResponse(
    val watchList: Int = 0,
    val watching: Int = 0,
    val finished: Int = 0,
)

@Serializable
data class LikedAnimeResponse(
    val animeId: Long? = null,
    val animeLikeId: Int? = null,
    val title: String,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

@Serializable
data class LikedPersonResponse(
    val personId: Long,
    val userLikedVoiceActorId: Int? = null,
    val name: String,
    val profileImageUrl: String,
)

fun MyPageResponse.toMyPageProfile(): MyPageProfile = MyPageProfile(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    watchCounts = WatchCounts(
        watchList = watchCounts.watchList,
        watching = watchCounts.watching,
        finished = watchCounts.finished,
    ),
    likedAnimes = likedAnimes.map { it.toAnime() },
    likedPersons = likedPersons.map { it.toActor() },
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
