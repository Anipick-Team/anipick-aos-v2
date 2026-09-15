package com.jparkbro.catalog.impl.studio

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.catalog.impl.studio.components.StudioAnimeSectionGrid
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogStudioRoot(
    studioId: Long,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: CatalogStudioViewModel = koinViewModel(parameters = { parametersOf(studioId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogStudioScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogStudioAction.Navigation -> when (action) {
                    CatalogStudioAction.OnBackClick -> onBackClick()
                    is CatalogStudioAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogStudioScreen(
    state: CatalogStudioState,
    onAction: (CatalogStudioAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = state.studioName ?: "제작사",
                onBackClick = { onAction(CatalogStudioAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        if (state.isLoading) {
            AniPickAnimeGridSkeleton(
                modifier = Modifier.padding(innerPadding),
                itemCount = 18,
            )
        } else {
            StudioAnimeSectionGrid(
                animes = state.animes,
                isLoadingMore = state.isLoadingMore,
                onAnimeClick = { anime -> anime.animeId?.let { onAction(CatalogStudioAction.OnAnimeClick(it)) } },
                onLoadMore = { onAction(CatalogStudioAction.OnLoadMore) },
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = BottomEdgeShadowClearance),
            )
        }
    }
}

private val previewAnimes = listOf(
    Anime(animeId = 1L, title = "샘플 애니메이션 1", coverImageUrl = "", seasonYear = "2025"),
    Anime(animeId = 2L, title = "샘플 애니메이션 2", coverImageUrl = "", seasonYear = "2025"),
    Anime(animeId = 3L, title = "샘플 애니메이션 3", coverImageUrl = "", seasonYear = "2025"),
    Anime(animeId = 4L, title = "샘플 애니메이션 4", coverImageUrl = "", seasonYear = "2024"),
    Anime(animeId = 5L, title = "샘플 애니메이션 5", coverImageUrl = "", seasonYear = "2024"),
)

@Composable
@Preview(showBackground = true)
private fun CatalogStudioScreenPreview() {
    CatalogStudioScreen(
        state = CatalogStudioState(studioId = 1L, studioName = "ufotable", animes = previewAnimes),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CatalogStudioScreenSkeletonPreview() {
    CatalogStudioScreen(
        state = CatalogStudioState(studioId = 1L, isLoading = true),
        onAction = {},
    )
}
