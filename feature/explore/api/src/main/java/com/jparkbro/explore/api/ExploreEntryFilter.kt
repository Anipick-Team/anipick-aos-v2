package com.jparkbro.explore.api

/** Explore 진입 시 적용할 초기 년도/분기 필터를 다른 화면에서 미리 담아두는 곳.
 *
 * [ExploreNavKey.Explore]는 바텀 네비 최상위 탭이라 파라미터를 실을 수 없다 — `Navigator`가 최상위 탭을
 * NavKey 인스턴스 동등성으로 식별하기 때문에(`key in state.topLevelKeys`), 파라미터가 붙은 인스턴스는 그
 * 동등성 검사를 통과하지 못해 탭 전환이 아니라 현재 탭 서브스택에 그냥 push되어버린다. 그래서 NavKey 대신
 * 이 객체에 값을 실어두고, Explore가 뜬 직후 [consume]으로 한 번만 읽어 초기 상태에 반영한다. */
object ExploreEntryFilter {

    data class Filter(val year: Int?, val season: Int?)

    private var pending: Filter? = null

    fun set(year: Int?, season: Int?) {
        pending = Filter(year, season)
    }

    /** 값을 읽고 즉시 비운다 - Explore 화면 재진입 시 같은 값이 반복 적용되지 않게. */
    fun consume(): Filter? {
        val value = pending
        pending = null
        return value
    }
}
