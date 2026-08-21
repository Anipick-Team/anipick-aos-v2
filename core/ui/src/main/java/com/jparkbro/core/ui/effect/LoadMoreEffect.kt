package com.jparkbro.core.ui.effect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/** 스크롤이 끝에서 [threshold]번째 아이템에 닿으면 [onLoadMore] 호출 */
@Composable
fun LoadMoreEffect(
    state: LazyGridState,
    threshold: Int,
    onLoadMore: () -> Unit,
) {
    val shouldLoadMore by remember(state, threshold) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
        }
    }

    LaunchedEffect(state, threshold) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }
}

/** [LazyListState] 버전 */
@Composable
fun LoadMoreEffect(
    state: LazyListState,
    threshold: Int = 3,
    onLoadMore: () -> Unit,
) {
    val shouldLoadMore by remember(state, threshold) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
        }
    }

    LaunchedEffect(state, threshold) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }
}
