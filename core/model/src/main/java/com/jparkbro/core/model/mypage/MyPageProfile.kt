package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime

data class MyPageProfile(
    val nickname: String,
    val profileImageUrl: String? = null,
    val watchCounts: WatchCounts = WatchCounts(),
    val likedAnimes: List<Anime> = emptyList(),
    val likedPersons: List<Actor> = emptyList(),
)

data class WatchCounts(
    val watchList: Int = 0,
    val watching: Int = 0,
    val finished: Int = 0,
)
