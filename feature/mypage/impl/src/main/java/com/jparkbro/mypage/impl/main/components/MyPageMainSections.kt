package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.component.AniPickAnimeCard
import com.jparkbro.core.ui.component.AniPickSectionHeader
import com.jparkbro.mypage.api.MyPageDetailType
import com.jparkbro.mypage.impl.main.MyPageMainAction
import com.jparkbro.mypage.impl.main.MyPageMainState

private val SECTION_ICON_SIZE = 18.dp

/** 마이페이지 메인 섹션들 - MyPageMainScreen LazyColumn의 item들. */
internal fun LazyListScope.myPageMainSections(
    state: MyPageMainState,
    onAction: (MyPageMainAction) -> Unit,
) {
    item {
        FeedbackLinkCard(
            modifier = Modifier,
            onFeedbackClick = { }
        )
    }
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            WatchStatusBox(
                title = "볼 애니",
                count = state.watchCounts.watchList ?: 0,
                onAction = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.WatchList)) }
            )
            WatchStatusBox(
                title = "보는 중",
                count = state.watchCounts.watching ?: 0,
                onAction = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.Watching)) }
            )
            WatchStatusBox(
                title = "다 본 애니",
                count = state.watchCounts.finished ?: 0,
                onAction = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.Finished)) }
            )
        }
    }
    item {
        AniPickSectionHeader(
            title = "평가한 작품",
            onMoreClick = { onAction(MyPageMainAction.OnRatedAnimesClick) },
            iconSize = SECTION_ICON_SIZE,
            iconTint = AniPickTheme.colors.textGray,
        )
    }
    item {
        AniPickSectionHeader(
            title = "내 콘텐츠",
            onMoreClick = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.MyContent)) },
            iconSize = SECTION_ICON_SIZE,
            iconTint = AniPickTheme.colors.textGray,
        )
    }
    item {
        MyPageListSection(
            title = "좋아요한 작품",
            items = state.likedAnimes,
            onMoreClick = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.LikedAnimes)) },
            emptyContentText = "아직 좋아요한 작품이 없어요.\n좋아요를 누르러 가볼까요 ?",
            itemKey = { it.animeId ?: it.hashCode() },
        ) { anime ->
            AniPickAnimeCard(
                anime = anime,
                cardWidth = 114.dp,
                onClick = { anime.animeId?.let { onAction(MyPageMainAction.OnAnimeClick(it)) } },
            )
        }
    }
    item {
        MyPageListSection(
            title = "좋아요한 인물",
            items = state.likedPersons,
            onMoreClick = { onAction(MyPageMainAction.OnDetailClick(MyPageDetailType.LikedPersons)) },
            emptyContentText = "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요 ?",
            itemKey = { it.personId },
        ) { actor ->
            LikedPersonCard(actor = actor, onClick = { onAction(MyPageMainAction.OnPersonClick(actor.personId)) })
        }
    }
}
