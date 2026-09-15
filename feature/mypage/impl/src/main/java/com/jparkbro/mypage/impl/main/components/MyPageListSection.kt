package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.ui.component.AniPickActorCard
import com.jparkbro.core.ui.component.AniPickSectionHeader

private val MYPAGE_SECTION_ICON_SIZE = 18.dp

@Composable
internal fun <T> MyPageListSection(
    title: String,
    items: List<T>,
    onMoreClick: () -> Unit,
    emptyContentText: String,
    modifier: Modifier = Modifier,
    itemKey: ((T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    val hasContent = items.isNotEmpty()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickSectionHeader(
            title = title,
            onMoreClick = onMoreClick,
            enabled = hasContent,
            iconSize = MYPAGE_SECTION_ICON_SIZE,
            iconTint = AniPickTheme.colors.textGray,
        )

        if (hasContent) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items, key = itemKey) { item ->
                    itemContent(item)
                }
            }
        } else {
            EmptyItems(contentText = emptyContentText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageListSectionPreview() {
    MyPageListSection(
        title = "좋아요한 인물",
        items = listOf(
            Actor(personId = 1, name = "카지 유우키", profileImage = ""),
            Actor(personId = 2, name = "하나자와 카나", profileImage = ""),
        ),
        onMoreClick = {},
        // 실제 사용처(MyPageMainScreen)에서는 "작품" 대신 "인물"만 바꿔서 넘긴다.
        emptyContentText = "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요 ?",
    ) { actor ->
        AniPickActorCard(actor = actor, cardWidth = 114.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageListSectionEmptyPreview() {
    MyPageListSection<Actor>(
        title = "좋아요한 인물",
        items = emptyList(),
        onMoreClick = {},
        // 실제 사용처(MyPageMainScreen)에서는 "작품" 대신 "인물"만 바꿔서 넘긴다.
        emptyContentText = "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요 ?",
        itemContent = { actor -> AniPickActorCard(actor = actor, cardWidth = 114.dp) },
    )
}
