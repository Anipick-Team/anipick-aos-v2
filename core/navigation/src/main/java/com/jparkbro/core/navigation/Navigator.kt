package com.jparkbro.core.navigation

import androidx.navigation3.runtime.NavKey

/** 네비게이션 이벤트를 처리해 상태를 업데이트하는 클래스 */
class Navigator(val state: NavigationState) {

    /** 목적지로 이동
     *  현재 탭 재탭: 서브 스택 초기화, 새 탑레벨 탭: 탭 전환, 그 외: 서브 화면으로 이동 */
    fun navigate(key: NavKey) {
        when (key) {
            state.currentTopLevelKey -> clearSubStack()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    /** 현재 스택을 비우고 [key] 하나만 남도록 이동 */
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

    /** 모든 탭의 서브스택을 루트로 되돌리고 [key]로 이동 */
    fun navigateAndClearAllStacks(key: NavKey) {
        check(key in state.topLevelKeys) { "$key 는 탑레벨 키가 아니라서 전체 스택을 초기화할 수 없습니다." }
        state.topLevelStack.apply {
            clear()
            add(key)
        }
        state.subStacks.forEach { (stackKey, stack) ->
            stack.apply {
                clear()
                add(stackKey)
            }
        }
    }

    /** 이전 화면으로 돌아가기
     *  갈 곳 있음: true, 더 갈 곳 없음: false */
    fun goBack(): Boolean {
        if (state.currentSubStack.size > 1) {
            state.currentSubStack.removeLastOrNull()
            return true
        }
        if (state.topLevelStack.size > 1) {
            state.topLevelStack.removeLastOrNull()
            return true
        }
        return false
    }

    /** 서브 화면으로 이동 */
    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    /** 메인 탭으로 이동 */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            clear()
            add(key)
        }
    }

    /** 현재 탭의 루트 화면만 남기고 나머지 서브 화면 제거 */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
