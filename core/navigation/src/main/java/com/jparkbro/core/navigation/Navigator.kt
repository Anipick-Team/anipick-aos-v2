package kr.agromarket.at.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * 네비게이션 이벤트(앞으로 가기, 뒤로 가기)를 처리하여 상태를 업데이트합니다.
 *
 * @param state 업데이트할 네비게이션 상태 객체.
 */
class Navigator(val state: NavigationState) {

    /**
     * 특정 목적지로 이동합니다.
     *
     * @param key 이동하고자 하는 목적지 키.
     */
    fun navigate(key: NavKey) {
        when (key) {
            // 현재 탭을 다시 누른 경우: 해당 탭의 쌓인 화면들을 모두 비움 (Root로 이동)
            state.currentTopLevelKey -> clearSubStack()
            // 새로운 메인 탭으로 이동하는 경우
            in state.topLevelKeys -> goToTopLevel(key)
            // 일반 화면(서브 화면)으로 이동하는 경우
            else -> goToKey(key)
        }
    }

    /**
     * 이전 화면으로 돌아갑니다.
     *
     * @return 뒤로 이동했다면 true, 이미 시작 화면이라 더 이상 갈 곳이 없다면 false.
     *         false인 경우 호출부에서 액티비티 종료 등을 처리해야 합니다.
     */
    fun goBack(): Boolean {
        return when (state.currentKey) {
            // 시작 화면이면 더 이상 뒤로 갈 수 없음
            state.startKey -> false
            // 현재 탭의 루트 화면인 경우: 이전 탭으로 이동
            state.currentTopLevelKey -> {
                state.topLevelStack.removeLastOrNull()
                true
            }
            // 탭 내부의 서브 화면인 경우: 서브 스택에서 제거
            else -> {
                state.currentSubStack.removeLastOrNull()
                true
            }
        }
    }

    /**
     * 서브 화면(Non-top level)으로 이동 로직
     */
    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            // 동일한 화면이 이미 스택에 있다면 제거 후 마지막에 추가 (중복 방지 및 순서 갱신)
            remove(key)
            add(key)
        }
    }

    /**
     * 메인 탭(Top level)으로 이동 로직
     */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) {
                // 시작 화면으로 가는 경우 스택을 모두 비워 앱 종료 직전 상태로 만듦
                clear()
            } else {
                // 이미 스택에 있는 탭이라면 위치만 조정
                remove(key)
            }
            add(key)
        }
    }

    /**
     * 현재 탭의 루트 화면만 남기고 나머지 서브 화면들을 모두 제거합니다.
     */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
