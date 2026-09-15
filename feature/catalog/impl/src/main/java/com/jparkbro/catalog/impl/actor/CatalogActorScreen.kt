package com.jparkbro.catalog.impl.actor

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.catalog.impl.actor.components.CatalogActorContent
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.ActorWork
import com.jparkbro.core.ui.component.AniPickAnimeGridSkeleton
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogActorRoot(
    personId: Long,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: CatalogActorViewModel = koinViewModel(parameters = { parametersOf(personId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogActorScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogActorAction.Navigation -> when (action) {
                    CatalogActorAction.OnBackClick -> onBackClick()
                    is CatalogActorAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogActorScreen(
    state: CatalogActorState,
    onAction: (CatalogActorAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "성우",
                onBackClick = { onAction(CatalogActorAction.OnBackClick) },
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
            CatalogActorContent(
                state = state,
                onAction = onAction,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = BottomEdgeShadowClearance),
            )
        }
    }
}

private val previewWorks = (1..15).map { id ->
    ActorWork(
        animeId = id.toLong(),
        animeTitle = "샘플 애니메이션 $id",
        characterId = id.toLong(),
        characterName = "샘플 캐릭터 $id",
        characterImageUrl = "",
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogActorScreenPreview() {
    CatalogActorScreen(
        state = CatalogActorState(
            personId = 1L,
            name = "카지 유우키",
            count = previewWorks.size,
            works = previewWorks,
        ),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogActorScreenSkeletonPreview() {
    CatalogActorScreen(
        state = CatalogActorState(personId = 1L, isLoading = true),
        onAction = {},
    )
}
