package com.jparkbro.explore.api

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/** Explore 진입 시 적용할 년도/분기 필터를 다른 화면에서 넘기는 곳.
 *
 * [ExploreNavKey.Explore]는 바텀 네비 최상위 탭이라 파라미터를 실을 수 없다 — `Navigator`가 최상위 탭을
 * NavKey 인스턴스 동등성으로 식별하기 때문에(`key in state.topLevelKeys`), 파라미터가 붙은 인스턴스는 그
 * 동등성 검사를 통과하지 못해 탭 전환이 아니라 현재 탭 서브스택에 그냥 push되어버린다. 그래서 NavKey 대신
 * 이 객체에 값을 실어둔다. Explore ViewModel이 아직 없으면 생성 직후 [consume]으로, 탭 유지로 이미 살아
 * 있으면 [filters] 수집으로 한 번씩만 받아 반영한다. */
object ExploreEntryFilter {

    data class Filter(val year: Int?, val season: Int?)

    private val channel = Channel<Filter>(Channel.CONFLATED)

    /** 이미 살아 있는 Explore ViewModel이 수집하는 스트림. 값은 한 번만 전달된다. */
    val filters: Flow<Filter> = channel.receiveAsFlow()

    fun set(year: Int?, season: Int?) {
        channel.trySend(Filter(year, season))
    }

    /** 대기 중인 값을 읽고 즉시 비운다 - ViewModel 생성 시점의 초기 상태 반영용. */
    fun consume(): Filter? = channel.tryReceive().getOrNull()
}
