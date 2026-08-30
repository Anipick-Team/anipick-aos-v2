package com.jparkbro.mypage.impl.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.mypage.api.MyPageDetailType
import com.jparkbro.mypage.impl.detail.components.MyContentTabContent
import com.jparkbro.mypage.impl.detail.components.MyPageAnimeGrid
import com.jparkbro.mypage.impl.detail.components.MyPagePersonGrid
import com.jparkbro.mypage.impl.detail.components.MyRatingDescription
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MyPageDetailRoot(
    type: MyPageDetailType,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToActorDetail: (Long) -> Unit,
    onNavigateToPostDetail: (Long) -> Unit,
    viewModel: MyPageDetailViewModel = koinViewModel(parameters = { parametersOf(type) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyPageDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is MyPageDetailAction.Navigation -> when (action) {
                    MyPageDetailAction.OnBackClick -> onBackClick()
                    is MyPageDetailAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                    is MyPageDetailAction.OnPersonClick -> onNavigateToActorDetail(action.personId)
                    is MyPageDetailAction.OnPostClick -> onNavigateToPostDetail(action.postId)
                    is MyPageDetailAction.OnCommentClick -> onNavigateToPostDetail(action.postId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun MyPageDetailScreen(
    state: MyPageDetailState,
    onAction: (MyPageDetailAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = state.type.title(),
                onBackClick = { onAction(MyPageDetailAction.OnBackClick) },
            )
        },
        containerColor = when (state.type) {
            MyPageDetailType.MyContent -> AniPickTheme.colors.backgroundGray
            else -> AniPickTheme.colors.white
        },
    ) { innerPadding ->
        when (state.type) {
            MyPageDetailType.WatchList,
            MyPageDetailType.Watching,
            MyPageDetailType.Finished,
            MyPageDetailType.LikedAnimes,
            -> MyPageAnimeGrid(
                animes = state.animes,
                totalCount = state.totalCount,
                isLoading = state.isLoading,
                onAnimeClick = { onAction(MyPageDetailAction.OnAnimeClick(it)) },
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                isLoadingMore = state.isLoadingMore,
                onLoadMore = { onAction(MyPageDetailAction.OnLoadMore) },
                emptyMessage = state.error ?: state.type.emptyMessage(),
                onRetryClick = state.error?.let { { onAction(MyPageDetailAction.OnRetryClick) } },
                cardDescription = if (state.type == MyPageDetailType.Finished) {
                    { anime -> MyRatingDescription(rating = anime.myRating) }
                } else {
                    null
                },
            )

            MyPageDetailType.LikedPersons -> MyPagePersonGrid(
                persons = state.persons,
                totalCount = state.totalCount,
                isLoading = state.isLoading,
                onPersonClick = { onAction(MyPageDetailAction.OnPersonClick(it)) },
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                isLoadingMore = state.isLoadingMore,
                onLoadMore = { onAction(MyPageDetailAction.OnLoadMore) },
                emptyMessage = state.error ?: state.type.emptyMessage(),
                onRetryClick = state.error?.let { { onAction(MyPageDetailAction.OnRetryClick) } },
            )

            MyPageDetailType.MyContent -> MyContentTabContent(
                state = state,
                onAction = onAction,
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MyPageDetailScreenGridPreview() {
    MyPageDetailScreen(
        state = MyPageDetailState(type = MyPageDetailType.LikedAnimes),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun MyPageDetailScreenMyContentPreview() {
    MyPageDetailScreen(
        state = MyPageDetailState(type = MyPageDetailType.MyContent),
        onAction = {},
    )
}
