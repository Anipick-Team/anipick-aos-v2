package com.jparkbro.community.impl.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.community.impl.main.components.CommunityAnimeInfoSection
import com.jparkbro.community.impl.main.components.CommunityPostItem
import com.jparkbro.community.impl.main.components.CommunityPostItemSkeleton
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.icon.Edit
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.ui.effect.LoadMoreEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val POST_SKELETON_ITEM_COUNT = 4

@Composable
internal fun CommunityMainRoot(
    seriesId: Long,
    title: String?,
    coverImageUrl: String?,
    genres: List<String>?,
    onBackClick: () -> Unit,
    onNavigateToPostDetail: (Long) -> Unit,
    onNavigateToPostWrite: () -> Unit,
    viewModel: CommunityMainViewModel = koinViewModel(
        parameters = { parametersOf(seriesId, title, coverImageUrl, genres) },
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CommunityMainScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CommunityMainAction.Navigation -> when (action) {
                    CommunityMainAction.OnBackClick -> onBackClick()
                    CommunityMainAction.OnWriteClick -> onNavigateToPostWrite()
                    is CommunityMainAction.OnPostClick -> onNavigateToPostDetail(action.postId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CommunityMainScreen(
    state: CommunityMainState,
    onAction: (CommunityMainAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LoadMoreEffect(state = listState, threshold = 3) {
        onAction(CommunityMainAction.OnLoadMorePosts)
    }

    // 필터(정렬) 바뀌면 내용은 완전히 새로 불러오는데 스크롤 위치는 그대로 남아있어서, 이전 스크롤
    // 위치에 새 목록이 겹쳐 보이는 문제가 있었다 - 필터 바뀔 때마다 맨 위로 되돌린다.
    LaunchedEffect(state.postFilter) {
        listState.scrollToItem(0)
    }

    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "커뮤니티",
                onBackClick = { onAction(CommunityMainAction.OnBackClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(CommunityMainAction.OnWriteClick) },
                shape = CircleShape,
                containerColor = AniPickTheme.colors.primary,
                contentColor = AniPickTheme.colors.white,
            ) {
                Icon(
                    imageVector = Edit,
                    contentDescription = "글쓰기",
                )
            }
        },
        containerColor = AniPickTheme.colors.backgroundGray,
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = BottomEdgeShadowClearance),
            contentPadding = PaddingValues(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CommunityAnimeInfoSection(
                    title = state.boardTitle,
                    coverImageUrl = state.boardCoverImageUrl,
                    genres = state.boardGenres,
                    postFilter = state.postFilter,
                    isSpoilerVisible = state.isSpoilerVisible,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AniPickTheme.colors.white)
                        .padding(top = 40.dp, bottom = 20.dp)
                        .padding(horizontal = 20.dp),
                )
            }
            if (state.isPostsLoading) {
                items(POST_SKELETON_ITEM_COUNT) {
                    CommunityPostItemSkeleton(modifier = Modifier.padding(horizontal = 20.dp))
                }
            } else if (state.visiblePosts.isEmpty()) {
                item {
                    val message = when {
                        state.error != null -> state.error
                        state.posts.isNotEmpty() -> "스포일러 게시글만 있어요.\n스포일러 노출을 켜보세요."
                        else -> "아직 작성된 게시글이 없어요.\n첫 글을 남겨보세요."
                    }
                    AniPickEmptyState(
                        message = message,
                        onRetryClick = state.error?.let { { onAction(CommunityMainAction.OnRetryClick) } },
                        modifier = Modifier.fillParentMaxHeight(0.6f),
                    )
                }
            } else {
                items(state.visiblePosts, key = { it.postId }) { post ->
                    CommunityPostItem(
                        post = post,
                        onClick = { onAction(CommunityMainAction.OnPostClick(post.postId)) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityMainScreenPreview() {
    CommunityMainScreen(
        state = CommunityMainState(
            seriesId = 1L,
            boardTitle = "프리렌: 장송의 여행",
            boardCoverImageUrl = "",
            boardGenres = listOf("판타지", "모험"),
            posts = listOf(
                CommunityPost(
                    postId = 1L,
                    nickname = "anipick_x2k3",
                    title = "이번 화 진짜 미쳤다",
                    content = "이번 화 전개 보고 소름 돋았음... 다들 봤어?",
                    isSpoiler = true,
                    viewCount = 102,
                    likeCount = 32,
                    commentCount = 8,
                    createdAt = "2025. 04. 03",
                    thumbnailImageUrl = "",
                    imageCount = 4,
                ),
                CommunityPost(
                    postId = 2L,
                    nickname = "frieren_fan",
                    title = "같이 볼 사람",
                    content = "이번 주말에 정주행하려는데 같이 보실 분",
                    viewCount = 40,
                    likeCount = 5,
                    commentCount = 2,
                    createdAt = "2025. 04. 02",
                ),
            ),
        ),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityMainScreenSpoilerHiddenPreview() {
    CommunityMainScreen(
        state = CommunityMainState(
            seriesId = 1L,
            boardTitle = "프리렌: 장송의 여행",
            boardCoverImageUrl = "",
            boardGenres = listOf("판타지", "모험"),
            isSpoilerVisible = false,
            posts = listOf(
                CommunityPost(
                    postId = 1L,
                    nickname = "anipick_x2k3",
                    title = "이번 화 진짜 미쳤다",
                    content = "이번 화 전개 보고 소름 돋았음... 다들 봤어?",
                    isSpoiler = true,
                    viewCount = 102,
                    likeCount = 32,
                    commentCount = 8,
                    createdAt = "2025. 04. 03",
                ),
                CommunityPost(
                    postId = 2L,
                    nickname = "frieren_fan",
                    title = "같이 볼 사람",
                    content = "이번 주말에 정주행하려는데 같이 보실 분",
                    viewCount = 40,
                    likeCount = 5,
                    commentCount = 2,
                    createdAt = "2025. 04. 02",
                ),
            ),
        ),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityMainScreenSkeletonPreview() {
    CommunityMainScreen(
        state = CommunityMainState(
            seriesId = 1L,
            boardTitle = "프리렌: 장송의 여행",
            boardCoverImageUrl = "",
            boardGenres = listOf("판타지", "모험"),
            isPostsLoading = true,
        ),
        onAction = {},
    )
}
