package com.jparkbro.home.impl.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import com.jparkbro.core.ui.util.DevicePreviews
import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.detail.components.DetailHeader
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun DetailRoot(
    type: HomeDetailType,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: HomeDetailViewModel = koinViewModel(parameters = { parametersOf(type) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomeDetailAction.OnBackClick -> onBackClick()
                is HomeDetailAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                is HomeDetailAction.OnDaySelected -> viewModel.onAction(action)
                is HomeDetailAction.OnSortSelected -> viewModel.onAction(action)
                HomeDetailAction.OnLoadMore -> viewModel.onAction(action)
                HomeDetailAction.OnRetryClick -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun DetailScreen(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = state.type.title(),
                onBackClick = { onAction(HomeDetailAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        when {
            state.isLoading -> AniPickAnimeGridSkeleton(
                modifier = Modifier.padding(innerPadding),
                itemCount = 18,
            )
            state.animes.isEmpty() -> AniPickEmptyState(
                message = state.error ?: "표시할 작품이 없습니다.",
                onRetryClick = state.error?.let { { onAction(HomeDetailAction.OnRetryClick) } },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )
            else -> AnimeGridContent(
                state = state,
                onAction = onAction,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun AnimeGridContent(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    AniPickAnimeInfiniteGrid(
        animes = state.animes,
        onAnimeClick = { anime -> anime.animeId?.let { onAction(HomeDetailAction.OnAnimeClick(it)) } },
        modifier = modifier,
        onLoadMore = { onAction(HomeDetailAction.OnLoadMore) },
        header = { item(span = { GridItemSpan(maxLineSpan) }) { DetailHeader(state = state, onAction = onAction) } },
        footer = if (state.isLoadingMore) {
            { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
        } else {
            null
        },
    )
}

private fun HomeDetailType.title(): String = when (this) {
    is HomeDetailType.Recommendation -> "추천 애니메이션"
    HomeDetailType.Weekly -> "요일별 신작"
    HomeDetailType.ComingSoon -> "공개 예정"
}

private val previewAnimes = (1..15).map { id ->
    Anime(animeId = id.toLong(), title = "샘플 애니메이션 $id", coverImageUrl = "")
}

@DevicePreviews
@Composable
private fun DetailScreenAnimeGridPreview() {
    DetailScreen(
        state = HomeDetailState(type = HomeDetailType.Recommendation(), animes = previewAnimes),
        onAction = {},
    )
}

@DevicePreviews
@Composable
private fun DetailScreenAnimeGridSkeletonPreview() {
    DetailScreen(
        state = HomeDetailState(type = HomeDetailType.Recommendation(), isLoading = true),
        onAction = {},
    )
}

@DevicePreviews
@Composable
private fun DetailScreenConnectionErrorPreview() {
    DetailScreen(
        state = HomeDetailState(
            type = HomeDetailType.Recommendation(),
            error = "네트워크 연결을 확인해주세요.",
        ),
        onAction = {},
    )
}

@DevicePreviews
@Composable
private fun DetailScreenEmptyPreview() {
    DetailScreen(
        state = HomeDetailState(type = HomeDetailType.Recommendation()),
        onAction = {},
    )
}
