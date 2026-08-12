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
     * 현재 스택을 비우고 [key] 하나만 남도록 이동합니다.
     * 스플래시 → 로그인, 로그인 완료 → 홈처럼 "한 번 지나가면 뒤로가기로도
     * 다시 돌아오면 안 되는" 전환에 [navigate] 대신 사용합니다.
     *
     * @param key 목적지 키. 탑레벨 키(예: 홈)면 탑레벨 스택 전체를, 아니면 현재
     *            탭의 서브 스택을 비우고 이 키 하나만 남깁니다.
     */
    fun navigateAndClearStack(key: NavKey) {
        if (key in state.topLevelKeys) {
            state.topLevelStack.apply {
                clear()
                add(key)
            }
            state.subStacks[key]?.apply {
                clear()
                add(key)
            }
        } else {
            state.currentSubStack.apply {
                clear()
                add(key)
            }
        }
    }

    /**
     * 이전 화면으로 돌아갑니다.
     * "여기가 더 갈 곳 없는 바닥인지"를 값 비교가 아니라 스택 크기로 판단합니다 —
     * [navigateAndClearStack]으로 스택 내용이 바뀌어도 항상 정확히 동작하도록.
     *
     * @return 뒤로 이동했다면 true, 더 이상 갈 곳이 없다면 false.
     *         false인 경우 호출부에서 액티비티 종료 등을 처리해야 합니다.
     */
    fun goBack(): Boolean {
        // 서브 스택에 쌓인 화면이 있으면 그것부터 제거
        if (state.currentSubStack.size > 1) {
            state.currentSubStack.removeLastOrNull()
            return true
        }
        // 서브 스택은 앵커 하나뿐 - 탑레벨 스택에 이전 탭이 있는지 확인
        if (state.topLevelStack.size > 1) {
            state.topLevelStack.removeLastOrNull()
            return true
        }
        // 더 이상 갈 곳 없음
        return false
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
     * 메인 탭(Top level)으로 이동 로직.
     * 탭끼리는 서로 히스토리를 안 쌓아야 하므로, 항상 탑레벨 스택을 비우고
     * 이동할 탭 하나만 남깁니다 (탭 전환 = push가 아니라 replace).
     */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            clear()
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
