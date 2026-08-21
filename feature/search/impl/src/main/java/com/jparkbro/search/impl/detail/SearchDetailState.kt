package com.jparkbro.search.impl.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.studio.Studio

data class SearchDetailState(
    val searchFieldState: TextFieldState = TextFieldState(),
    val searchType: SearchType = SearchType.ANIME,
    val animeCount: Int = 0,
    val actorCount: Int = 0,
    val studioCount: Int = 0,
    val animeResult: AnimeSearchResult = AnimeSearchResult(),
    val actorResult: ActorSearchResult = ActorSearchResult(),
    val studioResult: StudioSearchResult = StudioSearchResult(),
    val endReached: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class AnimeSearchResult(
    val animes: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val nextPage: Long? = null,
)

data class ActorSearchResult(
    val actors: List<Actor> = emptyList(),
    val cursor: Cursor? = null,
)

data class StudioSearchResult(
    val studios: List<Studio> = emptyList(),
    val cursor: Cursor? = null,
)

enum class SearchType {
    ANIME,
    ACTOR,
    STUDIO,
}
