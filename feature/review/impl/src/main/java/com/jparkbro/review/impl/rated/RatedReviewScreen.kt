package com.jparkbro.review.impl.rated

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.review.impl.rated.components.RatedReviewContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RatedReviewRoot(
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToWrite: (Long) -> Unit,
    viewModel: RatedReviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RatedReviewScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RatedReviewAction.Navigation -> when (action) {
                    RatedReviewAction.OnBackClick -> onBackClick()
                    is RatedReviewAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                    is RatedReviewAction.OnEditClick -> onNavigateToWrite(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun RatedReviewScreen(
    state: RatedReviewState,
    onAction: (RatedReviewAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "평가한 작품",
                onBackClick = { onAction(RatedReviewAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.backgroundGray,
    ) { innerPadding ->
        RatedReviewContent(
            state = state,
            onAction = onAction,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        )

        if (state.deleteTargetReviewId != null) {
            AniPickDialog(
                title = "리뷰 삭제",
                message = "이 리뷰를 삭제하시겠어요?",
                onDismissRequest = { onAction(RatedReviewAction.OnDeleteDismiss) },
                confirmText = "삭제",
                onConfirm = { onAction(RatedReviewAction.OnDeleteConfirm) },
                dismissText = "취소",
                onDismiss = { onAction(RatedReviewAction.OnDeleteDismiss) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RatedReviewScreenPreview() {
    RatedReviewScreen(
        state = RatedReviewState(),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun RatedReviewScreenLoadingPreview() {
    RatedReviewScreen(
        state = RatedReviewState(isLoading = true),
        onAction = {},
    )
}
