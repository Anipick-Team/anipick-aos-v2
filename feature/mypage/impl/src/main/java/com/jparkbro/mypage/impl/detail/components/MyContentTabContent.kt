package com.jparkbro.mypage.impl.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickSecondaryTabRow
import com.jparkbro.core.designsystem.model.AniPickTabItem
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.effect.LoadMoreEffect
import com.jparkbro.mypage.api.MyPageDetailType
import com.jparkbro.mypage.impl.detail.MyContentTab
import com.jparkbro.mypage.impl.detail.MyPageDetailAction
import com.jparkbro.mypage.impl.detail.MyPageDetailState

private val MY_CONTENT_TABS = listOf(
    AniPickTabItem(label = "내 게시글"),
    AniPickTabItem(label = "내 댓글"),
)
private const val SKELETON_ITEM_COUNT = 4

/** 마이페이지 "내가 쓴 글" - 게시글/댓글 탭 + 선택된 탭의 목록 */
@Composable
internal fun MyContentTabContent(
    state: MyPageDetailState,
    onAction: (MyPageDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LoadMoreEffect(state = listState, onLoadMore = { onAction(MyPageDetailAction.OnLoadMore) })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AniPickSecondaryTabRow(
            tabs = MY_CONTENT_TABS,
            selectedIndex = state.myContentTab.ordinal,
            onTabClick = { index -> onAction(MyPageDetailAction.OnMyContentTabSelected(MyContentTab.entries[index])) },
            modifier = Modifier
                .background(AniPickTheme.colors.white)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        )

        val isEmpty = when (state.myContentTab) {
            MyContentTab.POSTS -> state.posts.isEmpty()
            MyContentTab.COMMENTS -> state.comments.isEmpty()
        }
        val emptyMessage = state.error ?: when (state.myContentTab) {
            MyContentTab.POSTS -> "아직 작성한 게시글이 없어요."
            MyContentTab.COMMENTS -> "아직 작성한 댓글이 없어요."
        }

        if (state.isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
            ) {
                item {
                    AniPickCountLabel(
                        count = state.totalCount,
                        unit = "개",
                        contentPadding = PaddingValues(bottom = 4.dp),
                    )
                }
                items(SKELETON_ITEM_COUNT) {
                    when (state.myContentTab) {
                        MyContentTab.POSTS -> MyPagePostItemSkeleton()
                        MyContentTab.COMMENTS -> MyPageCommentItemSkeleton()
                    }
                }
            }
        } else if (isEmpty) {
            Column(modifier = Modifier.fillMaxSize()) {
                AniPickCountLabel(
                    count = state.totalCount,
                    unit = "개",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 4.dp),
                )
                AniPickEmptyState(
                    message = emptyMessage,
                    onRetryClick = state.error?.let { { onAction(MyPageDetailAction.OnRetryClick) } },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    AniPickCountLabel(
                        count = state.totalCount,
                        unit = "개",
                        contentPadding = PaddingValues(bottom = 4.dp),
                    )
                }
                when (state.myContentTab) {
                    MyContentTab.POSTS -> items(state.posts, key = { it.postId }) { post ->
                        MyPagePostItem(post = post, onClick = { onAction(MyPageDetailAction.OnPostClick(post.postId)) })
                    }
                    MyContentTab.COMMENTS -> items(state.comments, key = { it.commentId }) { comment ->
                        MyPageCommentItem(
                            comment = comment,
                            onClick = { comment.postId?.let { onAction(MyPageDetailAction.OnCommentClick(it)) } },
                        )
                    }
                }
                if (state.isLoadingMore) {
                    item { AniPickLoadMoreIndicator() }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MyContentTabContentEmptyPreview() {
    MyContentTabContent(
        state = MyPageDetailState(type = MyPageDetailType.MyContent),
        onAction = {},
    )
}
