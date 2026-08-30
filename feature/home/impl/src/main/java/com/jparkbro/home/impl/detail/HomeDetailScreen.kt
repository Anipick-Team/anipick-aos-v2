package com.jparkbro.home.impl.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import com.jparkbro.core.ui.util.DevicePreviews
import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.detail.components.AnimeGridContent
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
                is HomeDetailAction.Navigation -> when (action) {
                    HomeDetailAction.OnBackClick -> onBackClick()
                    is HomeDetailAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
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
