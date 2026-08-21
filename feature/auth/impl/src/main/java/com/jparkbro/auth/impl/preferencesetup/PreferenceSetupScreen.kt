package com.jparkbro.auth.impl.preferencesetup

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.preferencesetup.components.PreferenceSetupAnimeList
import com.jparkbro.auth.impl.preferencesetup.components.PreferenceSetupCompleteBar
import com.jparkbro.auth.impl.preferencesetup.components.PreferenceSetupHeader
import com.jparkbro.auth.impl.preferencesetup.components.PreferenceSetupSearchFilterSection
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.AniPickAnimeFilterBottomSheet
import com.jparkbro.core.ui.CollapsibleHeader
import com.jparkbro.core.ui.LoadMoreEffect
import com.jparkbro.core.ui.ObserveAsEvents
import com.jparkbro.core.ui.rememberCollapsibleHeaderState
import com.jparkbro.core.ui.toAnimeFilterTab
import org.koin.compose.viewmodel.koinViewModel

private const val LOAD_MORE_THRESHOLD = 3

@Composable
internal fun PreferenceSetupRoot(
    onNavigateToHome: () -> Unit,
    viewModel: PreferenceSetupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PreferenceSetupEvent.NavigateToHome -> onNavigateToHome()
        }
    }

    PreferenceSetupScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun PreferenceSetupScreen(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val searchHeaderState = rememberCollapsibleHeaderState()
    val lazyListState = rememberLazyListState()
    LoadMoreEffect(state = lazyListState, threshold = LOAD_MORE_THRESHOLD) {
        onAction(PreferenceSetupAction.OnLoadMore)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = AniPickTheme.colors.white
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 20.dp)
        ) {
            PreferenceSetupHeader(
                onSkipClick = { onAction(PreferenceSetupAction.OnSkipClick) },
                modifier = Modifier.fillMaxWidth(),
            )
            CollapsibleHeader(state = searchHeaderState) {
                PreferenceSetupSearchFilterSection(state = state, onAction = onAction)
            }
            PreferenceSetupAnimeList(
                state = state,
                onAction = onAction,
                lazyListState = lazyListState,
                nestedScrollConnection = searchHeaderState.nestedScrollConnection,
                modifier = Modifier.weight(1f),
            )
            PreferenceSetupCompleteBar(
                enabled = state.isCompleteEnabled,
                isLoading = state.isCompleting,
                onCompleteClick = { onAction(PreferenceSetupAction.OnCompleteClick) },
            )
        }
    }

    state.activeFilterSheet?.let { filterType ->
        AniPickAnimeFilterBottomSheet(
            years = state.metadata.seasonYears ?: emptyList(),
            seasons = state.metadata.seasons ?: emptyList(),
            genres = state.metadata.genres ?: emptyList(),
            types = state.metadata.types ?: emptyList(),
            initialYear = state.selectedYear,
            initialSeason = state.selectedSeason,
            initialGenre = state.selectedGenre,
            initialType = null,
            initialTab = filterType.toAnimeFilterTab(),
            showYearSeasonTab = true,
            showGenreTab = true,
            showTypeTab = false,
            onConfirm = { year, season, genre, type ->
                onAction(PreferenceSetupAction.OnAnimeFilterConfirm(year, season, genre, type))
            },
            onDismissRequest = { onAction(PreferenceSetupAction.OnFilterSheetDismiss) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferenceSetupScreenPreview() {
    PreferenceSetupScreen(
        state = PreferenceSetupState(),
        onAction = {}
    )
}
