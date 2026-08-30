package com.jparkbro.catalog.impl.character

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogCharacterRoot(
    animeId: Long,
    onBackClick: () -> Unit,
    viewModel: CatalogCharacterViewModel = koinViewModel(parameters = { parametersOf(animeId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogCharacterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogCharacterAction.Navigation -> when (action) {
                    CatalogCharacterAction.OnBackClick -> onBackClick()
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogCharacterScreen(
    state: CatalogCharacterState,
    onAction: (CatalogCharacterAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "캐릭터 / 성우진",
                onBackClick = { onAction(CatalogCharacterAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        // TODO: 등장인물/성우 목록(무한스크롤)으로 채운다.
        Text(
            text = "TODO: 등장인물 목록 (animeId=${state.animeId})",
            style = AniPickTheme.typography.body1,
            color = AniPickTheme.colors.textGray,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CatalogCharacterScreenPreview() {
    CatalogCharacterScreen(
        state = CatalogCharacterState(animeId = 1L),
        onAction = {},
    )
}
