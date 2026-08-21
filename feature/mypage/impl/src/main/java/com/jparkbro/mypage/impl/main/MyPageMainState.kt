package com.jparkbro.mypage.impl.main

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.mypage.WatchCounts

data class MyPageMainState(
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val watchCounts: WatchCounts = WatchCounts(),
    val likedAnimes: List<Anime> = emptyList(),
    val likedPersons: List<Actor> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
