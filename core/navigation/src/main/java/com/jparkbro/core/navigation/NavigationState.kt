package kr.agromarket.at.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration

/**
 * 설정 변경(회전 등) 및 프로세스 종료 시에도 유지되는 네비게이션 상태를 생성합니다.
 */
@Composable
fun rememberNavigationState(
    startKey: NavKey,           // 앱 시작 시 첫 화면(루트) 키
    topLevelKeys: Set<NavKey>,  // 바텀 네비게이션 등에 해당하는 최상위 화면 키 집합
): NavigationState {
    // 최상위 레벨의 백스택 (예: 홈, 통계, 설정 등 탭 간의 이동 기록)
    val topLevelStack = rememberNavBackStack(startKey)
    // 각 최상위 키마다 개별적인 서브 백스택을 생성하여 상태를 유지함
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }

    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks
        )
    }
}

/**
 * 네비게이션 상태를 보유하는 클래스입니다.
 *
 * @param startKey 시작 네비게이션 키. 사용자가 이 화면에서 뒤로가기를 하면 앱이 종료됩니다.
 * @param topLevelStack 최상위 백스택. 주요 탭들의 이동 기록만 보유합니다.
 * @param subStacks 각 최상위 키(탭)별로 내부에 쌓이는 화면들의 백스택 맵입니다.
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    // 현재 화면에 표시되고 있는 최상위 레벨 키 (현재 선택된 탭)
    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    val topLevelKeys
        get() = subStacks.keys

    // 현재 활성화된 탭의 서브 백스택
    @get:VisibleForTesting
    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks[currentTopLevelKey]
            ?: error("$currentTopLevelKey 에 대한 서브 스택이 존재하지 않습니다.")

    // 현재 사용자에게 보이고 있는 최종 목적지 키
    @get:VisibleForTesting
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}

/**
 * NavigationState를 Compose에서 처리 가능한 NavEntries 리스트로 변환합니다.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = subStacks.mapValues { (_, stack) ->
        // ViewModel 상태 및 Saveable 상태를 유지하기 위한 데코레이터 설정
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    // 최상위 스택의 순서에 따라 각 서브 스택의 엔트리들을 합쳐서 반환
    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
