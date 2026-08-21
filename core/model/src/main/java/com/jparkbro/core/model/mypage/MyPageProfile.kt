package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime

data class MyPageProfile(
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val watchCounts: WatchCounts? = null,
    val likedAnimes: List<Anime>? = null,
    val likedPersons: List<Actor>? = null,
)

data class WatchCounts(
    val watchList: Int? = null,
    val watching: Int? = null,
    val finished: Int? = null,
)
