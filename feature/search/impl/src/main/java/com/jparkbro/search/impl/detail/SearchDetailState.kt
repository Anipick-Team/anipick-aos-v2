package com.jparkbro.search.impl.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.studio.Studio

data class SearchDetailState(
    val searchFieldState: TextFieldState = TextFieldState(),
    val searchType: SearchType = SearchType.ANIME,
    /** 상단 탭에 표시하는 카테고리별 검색 결과 수 - 세 검색 API 모두 자기 카테고리 외 나머지 두
     *  카테고리의 개수도 같이 내려줘서, 어느 탭에서 검색해도 셋 다 갱신된다. */
    val animeCount: Int = 0,
    val actorCount: Int = 0,
    val studioCount: Int = 0,
    val animeResult: AnimeSearchResult = AnimeSearchResult(),
    val actorResult: ActorSearchResult = ActorSearchResult(),
    val studioResult: StudioSearchResult = StudioSearchResult(),
    /** 마지막 페이지까지 다 불러왔는지 - true면 더 요청하지 않는다. 탭을 전환하면 항상 새로
     *  조회하므로(같은 탭을 다시 누른 경우만 제외) 카테고리별로 따로 안 두고 하나만 둔다. */
    val endReached: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class AnimeSearchResult(
    val animes: List<Anime> = emptyList(),
    /** 다음 페이지 요청용 커서. */
    val cursor: Cursor? = null,
    /** 다음 페이지 요청용 페이지 번호 - 커서(lastId)와 같이 넘겨야 한다. */
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
