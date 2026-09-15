package com.jparkbro.catalog.impl.actor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.catalog.impl.actor.CatalogActorAction
import com.jparkbro.catalog.impl.actor.CatalogActorState
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.ActorWork
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.component.AniPickAnimeInfiniteGrid
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 성우 프로필 + 참여 작품 목록 그리드 - CatalogActorScreen 하나만 쓴다. */
@Composable
internal fun CatalogActorContent(
    state: CatalogActorState,
    onAction: (CatalogActorAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        CatalogActorProfileHeader(
            profileImageUrl = state.profileImageUrl,
            name = state.name,
            isLiked = state.isLiked,
            onLikeClick = { onAction(CatalogActorAction.OnLikeClick) },
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        AniPickSectionDivider()
        AniPickAnimeInfiniteGrid(
            animes = state.works.map { it.toDisplayAnime() },
            onAnimeClick = { anime -> anime.animeId?.let { onAction(CatalogActorAction.OnAnimeClick(it)) } },
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
            onLoadMore = { onAction(CatalogActorAction.OnLoadMore) },
            header = {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CatalogActorWorksHeader(count = state.count)
                }
            },
            footer = if (state.isLoadingMore) {
                { item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() } }
            } else {
                null
            },
        )
    }
}

/** 성우 프로필(사진/이름/찜) 헤더 - CatalogActorScreen 하나만 쓴다. */
@Composable
internal fun CatalogActorProfileHeader(
    profileImageUrl: String?,
    name: String?,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = profileImageUrl.orNullIfDefaultCover(),
            contentDescription = "성우 사진",
            error = painterResource(R.drawable.portrait_default_img),
            placeholder = painterResource(R.drawable.portrait_default_img),
            modifier = Modifier
                .width(132.dp)
                .aspectRatio(132f / 152f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name.orEmpty(),
            style = AniPickTheme.typography.body1,
            color = AniPickTheme.colors.black,
        )
        Spacer(modifier = Modifier.width(8.dp))
        AniPickAnimatedHeartIcon(
            isLiked = isLiked,
            onClick = onLikeClick,
            unlikedTint = AniPickTheme.colors.gray,
        )
    }
}

/** "참여 작품 목록 / 총 N개" 그리드 상단 헤더 - CatalogActorScreen 하나만 쓴다. */
@Composable
internal fun CatalogActorWorksHeader(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "참여 작품 목록",
            style = AniPickTheme.typography.h3,
            color = AniPickTheme.colors.black,
        )
        AniPickCountLabel(
            count = count,
            unit = "개",
            contentPadding = PaddingValues(0.dp),
        )
    }
}

/** 그리드는 [Anime] 카드를 그대로 쓴다 - 캐릭터 이미지/이름을 커버/제목 자리에, 애니 제목을 부제로 얹는다. */
internal fun ActorWork.toDisplayAnime(): Anime = Anime(
    animeId = animeId,
    title = characterName,
    coverImageUrl = characterImageUrl,
    subtitle = animeTitle,
)
