package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickAnimatedChevronIcon
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.ChevronDown
import com.jparkbro.core.designsystem.icon.ChevronUp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeDetail
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.character.Character
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.ui.component.AniPickAnimeCard
import com.jparkbro.core.ui.component.AniPickAnimeCardSkeleton
import com.jparkbro.core.ui.component.AniPickCardBackground
import com.jparkbro.core.ui.component.AniPickCastPairCard
import com.jparkbro.core.ui.component.AniPickCastPairCardSkeleton
import com.jparkbro.core.ui.component.AniPickSectionHeader
import com.jparkbro.core.ui.component.CARD_ASPECT_RATIO
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** "작품 정보" 탭 콘텐츠. 더보기 버튼은 description이 3줄 넘겨 잘릴 때만 노출된다. */
internal fun LazyListScope.animeInfoTabContent(
    detail: AnimeDetail,
    cast: List<AnimeCharacter>,
    series: List<Anime>,
    recommendations: List<Anime>,
    isDescriptionExpanded: Boolean,
    onToggleDescriptionExpanded: () -> Unit,
    onCastMoreClick: () -> Unit,
    onCastClick: (Long) -> Unit,
    onSeriesMoreClick: () -> Unit,
    onRecommendationMoreClick: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onStudioClick: (Long) -> Unit,
) {
    if (!detail.description.isNullOrBlank()) {
        item {
            var isOverflowing by remember(detail.description) { mutableStateOf(false) }

            Column {
                Text(
                    text = detail.description ?: "",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.black,
                    maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { result ->
                        if (!isDescriptionExpanded) isOverflowing = result.hasVisualOverflow
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                )

                if (isOverflowing) {
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                            .clickable(onClick = onToggleDescriptionExpanded),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (isDescriptionExpanded) "접기" else "더보기",
                            style = AniPickTheme.typography.body1,
                            color = AniPickTheme.colors.primary,
                        )
                        AniPickAnimatedChevronIcon(
                            isExpanded = isDescriptionExpanded,
                            contentDescription = "더보기/접기",
                            tint = AniPickTheme.colors.primary,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }

    item {
        AniPickSectionDivider(modifier = Modifier.padding(top = 20.dp))
    }

    item {
        Column(
            modifier = Modifier.padding(top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AnimeInfoRow(label = "타입", value = detail.type?.ifBlank { "-" } ?: "-")
                AnimeInfoRow(label = "장르") {
                    val genres = detail.genres
                    if (genres.isNullOrEmpty()) {
                        Text(
                            text = "-",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.textGray,
                        )
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            genres.forEach { genre -> AniPickGenreTag(genre = genre.name ?: "-") }
                        }
                    }
                }
                AnimeInfoRow(label = "방영 시기") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!detail.status.isNullOrEmpty()) {
                            Text(
                                text = detail.status ?: "",
                                style = AniPickTheme.typography.caption1,
                                color = AniPickTheme.colors.black,
                                modifier = Modifier
                                    .background(AniPickTheme.colors.lightGray, CircleShape)
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                            )
                        }
                        Text(
                            text = detail.airDate?.ifBlank { "-" } ?: "-",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.textGray,
                        )
                    }
                }
                AnimeInfoRow(label = "에피소드", value = detail.episode?.let { "${it}개" } ?: "-")
                AnimeInfoRow(label = "연령 등급", value = detail.age?.ifBlank { "-" } ?: "-")
                AnimeInfoRow(label = "제작사") {
                    val studios = detail.studios
                    if (studios.isNullOrEmpty()) {
                        Text(
                            text = "-",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.textGray,
                        )
                    } else {
                        val primaryColor = AniPickTheme.colors.primary
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            studios.forEach { studio ->
                                Text(
                                    text = studio.name ?: "-",
                                    style = AniPickTheme.typography.caption1,
                                    color = primaryColor,
                                    modifier = Modifier
                                        .clickable { onStudioClick(studio.studioId) }
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = primaryColor,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth,
                                            )
                                        },
                                )
                            }
                        }
                    }
                }
            }

            if (cast.isNotEmpty()) {
                AnimeInfoSection(title = "캐릭터 / 성우진", onMoreClick = onCastMoreClick) {
                    itemsIndexed(cast, key = { index, item -> "$index-${item.character?.characterId}" }) { _, animeCharacter ->
                        AniPickCastPairCard(
                            animeCharacter = animeCharacter,
                            cardWidth = 100.dp,
                            onClick = { animeCharacter.voiceActor?.personId?.let(onCastClick) },
                        )
                    }
                }
            }

            if (series.isNotEmpty()) {
                AnimeInfoSection(title = "시리즈 정보", onMoreClick = onSeriesMoreClick) {
                    itemsIndexed(series, key = { index, item -> "$index-${item.animeId}" }) { _, anime ->
                        AniPickAnimeCard(
                            anime = anime,
                            cardWidth = 114.dp,
                            background = AniPickCardBackground.GRAY,
                            onClick = { anime.animeId?.let(onAnimeClick) },
                        )
                    }
                }
            }

            if (recommendations.isNotEmpty()) {
                AnimeInfoSection(title = "함께 볼만한 작품", onMoreClick = onRecommendationMoreClick) {
                    itemsIndexed(recommendations, key = { index, item -> "$index-${item.animeId}" }) { _, anime ->
                        AniPickAnimeCard(
                            anime = anime,
                            cardWidth = 114.dp,
                            background = AniPickCardBackground.GRAY,
                            onClick = { anime.animeId?.let(onAnimeClick) },
                        )
                    }
                }
            }
        }
    }
    item {
        Spacer(modifier = Modifier.height(40.dp))
    }
}

private const val ANIME_INFO_SKELETON_ITEM_COUNT = 3

/** [animeInfoTabContent]와 동일 레이아웃의 로딩 스켈레톤. 라벨/섹션 제목은 고정값 그대로, 나머지는 셰이머로 대체 */
internal fun LazyListScope.animeInfoTabSkeleton() {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
        }
    }

    item {
        AniPickSectionDivider(modifier = Modifier.padding(top = 20.dp))
    }

    item {
        Column(
            modifier = Modifier.padding(top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AnimeInfoRowSkeleton(label = "타입", valueWidth = 60.dp)
                AnimeInfoRowSkeleton(label = "장르", valueWidth = 120.dp)
                AnimeInfoRowSkeleton(label = "방영 시기", valueWidth = 140.dp)
                AnimeInfoRowSkeleton(label = "에피소드", valueWidth = 60.dp)
                AnimeInfoRowSkeleton(label = "연령 등급", valueWidth = 60.dp)
                AnimeInfoRowSkeleton(label = "제작사", valueWidth = 100.dp)
            }

            AnimeInfoSection(title = "캐릭터 / 성우진", onMoreClick = {}, enabled = false) {
                items(ANIME_INFO_SKELETON_ITEM_COUNT) {
                    AniPickCastPairCardSkeleton(cardWidth = 100.dp)
                }
            }
            AnimeInfoSection(title = "시리즈 정보", onMoreClick = {}, enabled = false) {
                items(ANIME_INFO_SKELETON_ITEM_COUNT) {
                    AniPickAnimeCardSkeleton(cardWidth = 114.dp)
                }
            }
            AnimeInfoSection(title = "함께 볼만한 작품", onMoreClick = {}, enabled = false) {
                items(ANIME_INFO_SKELETON_ITEM_COUNT) {
                    AniPickAnimeCardSkeleton(cardWidth = 114.dp)
                }
            }
        }
    }
    item {
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/** 라벨 1 : 값 4 비율의 정보 행. */
@Composable
private fun AnimeInfoRow(
    label: String,
    modifier: Modifier = Modifier,
    value: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier.weight(4f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            value()
        }
    }
}

@Composable
private fun AnimeInfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    AnimeInfoRow(label = label, modifier = modifier) {
        Text(
            text = value,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.textGray,
        )
    }
}

/** [AnimeInfoRow]와 같은 라벨 1 : 값 4 비율에서, 값 자리만 [valueWidth] 너비의 셰이머로 채운다. */
@Composable
private fun AnimeInfoRowSkeleton(label: String, valueWidth: Dp, modifier: Modifier = Modifier) {
    AnimeInfoRow(label = label, modifier = modifier) {
        AniPickShimmerBox(modifier = Modifier.width(valueWidth).height(14.dp))
    }
}

/** 캐릭터/성우진, 시리즈, 추천 섹션이 공통으로 쓰는 "제목+더보기 아이콘" + 가로 스크롤 카드 목록. HomeMain 섹션과 동일한 규격. */
@Composable
private fun AnimeInfoSection(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AniPickSectionHeader(
            title = title,
            onMoreClick = onMoreClick,
            enabled = enabled,
            titlePadding = PaddingValues(horizontal = 20.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

private val PREVIEW_DETAIL = AnimeDetail(
    animeId = 1L,
    title = "장송의 프리렌 2기",
    description = "다솜 산들림 달볕 비나리 예그리나 아름드리 별빛 도담도담 소록소록 미리내 아라리 온새미로.",
    type = "TVA",
    episode = 12,
    airDate = "2025년 3분기",
    status = "방영 중",
    genres = listOf(Genre(id = 1, name = "판타지"), Genre(id = 2, name = "모험")),
)

private val PREVIEW_CAST = listOf(
    AnimeCharacter(
        character = Character(characterId = 1L, name = "프리렌", imageUrl = ""),
        voiceActor = Actor(personId = 1L, name = "타네자키 아츠미", profileImage = ""),
    ),
    AnimeCharacter(
        character = Character(characterId = 2L, name = "페른", imageUrl = ""),
        voiceActor = Actor(personId = 2L, name = "이토 아사미", profileImage = ""),
    ),
)

private val PREVIEW_SERIES = listOf(
    Anime(animeId = 1L, title = "장송의 프리렌 1기", coverImageUrl = ""),
    Anime(animeId = 2L, title = "장송의 프리렌 극장판", coverImageUrl = ""),
)

private val PREVIEW_RECOMMENDATIONS = listOf(
    Anime(animeId = 3L, title = "빙과", coverImageUrl = ""),
    Anime(animeId = 4L, title = "모브사이코 100", coverImageUrl = ""),
)

@Composable
@Preview(showBackground = true)
private fun AnimeInfoTabContentPreview() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        animeInfoTabContent(
            detail = PREVIEW_DETAIL,
            cast = PREVIEW_CAST,
            series = PREVIEW_SERIES,
            recommendations = PREVIEW_RECOMMENDATIONS,
            isDescriptionExpanded = false,
            onToggleDescriptionExpanded = {},
            onCastMoreClick = {},
            onCastClick = {},
            onSeriesMoreClick = {},
            onRecommendationMoreClick = {},
            onAnimeClick = {},
            onStudioClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AnimeInfoTabSkeletonPreview() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        animeInfoTabSkeleton()
    }
}
