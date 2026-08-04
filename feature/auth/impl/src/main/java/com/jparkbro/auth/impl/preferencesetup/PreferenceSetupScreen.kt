package com.jparkbro.auth.impl.preferencesetup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickFilterChip
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.component.AniPickStarRatingBar
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.icon.Search
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.ui.AnimeFilterTab
import com.jparkbro.core.ui.AniPickAnimeFilterBottomSheet
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

private val SEARCH_HIDE_SCROLL_THRESHOLD = 8.dp
private val SEARCH_SHOW_SCROLL_THRESHOLD = 16.dp
private const val LOAD_MORE_THRESHOLD = 3
private const val SEARCH_SKELETON_ITEM_COUNT = 8

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
    var isSearchVisible by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    val searchVisibilityScrollConnection = remember(density) {
        val hideThresholdPx = with(density) { SEARCH_HIDE_SCROLL_THRESHOLD.toPx() }
        val showThresholdPx = with(density) { SEARCH_SHOW_SCROLL_THRESHOLD.toPx() }
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -hideThresholdPx) isSearchVisible = false
                if (available.y > showThresholdPx) isSearchVisible = true
                return Offset.Zero
            }
        }
    }
    val lazyListState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            totalItemsCount > 0 && lastVisibleIndex >= totalItemsCount - 1 - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { shouldLoadMore }
            .collect { should -> if (should) onAction(PreferenceSetupAction.OnLoadMore) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "건너뛰기",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.textGray,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { onAction(PreferenceSetupAction.OnSkipClick) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "좋아하는 애니메이션을 편가해주세요.\n취향에 맞는 작품을 추천할게요.",
                    style = AniPickTheme.typography.h1,
                    color = AniPickTheme.colors.black,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "좋아하는 애니메이션을 골라 주세요.",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.primary,
                )
            }
            AnimatedVisibility(
                visible = isSearchVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "진행한 평가 수",
                            style = AniPickTheme.typography.body2,
                            color = AniPickTheme.colors.black,
                        )

                        Text(
                            text = state.ratedCount.toString(),
                            style = AniPickTheme.typography.body1,
                            color = AniPickTheme.colors.primary,
                        )
                    }
                    AniPickBaseTextField(
                        state = state.searchFieldState,
                        type = TextFieldType.TEXT,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        onKeyboardAction = {
                            focusManager.clearFocus()
                            onAction(PreferenceSetupAction.OnSearchClick)
                        },
                        placeholder = "무엇을 검색할까요?",
                        actions = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Search,
                                    tint = AniPickTheme.colors.textGray,
                                    contentDescription = "검색",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            focusManager.clearFocus()
                                            onAction(PreferenceSetupAction.OnSearchClick)
                                        }
                                )
                                Icon(
                                    imageVector = Close,
                                    tint = AniPickTheme.colors.textGray,
                                    contentDescription = "검색 초기화",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onAction(PreferenceSetupAction.OnSearchClearClick) }
                                )
                            }
                        },
                    )
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AniPickFilterChip(
                            text = state.selectedYear?.let { "${it}년" } ?: "년도",
                            isSelected = state.selectedYear != null,
                            isExpanded = state.activeFilterSheet == FilterType.YEAR,
                            onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.YEAR)) }
                        )
                        AniPickFilterChip(
                            text = state.selectedSeason?.name ?: "분기",
                            isSelected = state.selectedSeason != null,
                            isExpanded = state.activeFilterSheet == FilterType.SEASON,
                            onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.SEASON)) }
                        )
                        AniPickFilterChip(
                            text = state.selectedGenre?.name ?: "장르",
                            isSelected = state.selectedGenre != null,
                            isExpanded = state.activeFilterSheet == FilterType.GENRE,
                            onClick = { onAction(PreferenceSetupAction.OnFilterChipClick(FilterType.GENRE)) }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.weight(1f),
            ) {
                if (state.isSearchError) {
                    AniPickEmptyState(
                        message = "네트워크 연결을 확인해주세요.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                } else if (!state.isSearchLoading && state.animeList.isEmpty()) {
                    AniPickEmptyState(
                        message = "해당하는 작품이 없습니다.\n다른 조건으로 검색해보세요.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(searchVisibilityScrollConnection)
                            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        userScrollEnabled = !state.isSearchLoading,
                    ) {
                        if (state.isSearchLoading) {
                            items(SEARCH_SKELETON_ITEM_COUNT) {
                                PreferenceSetupAnimeItemSkeleton()
                            }
                        } else {
                            itemsIndexed(
                                items = state.animeList,
                                key = { _, anime -> anime.animeId ?: anime.hashCode() }
                            ) { index, anime ->
                                val committedRating = state.ratings.find { it.animeId == anime.animeId }?.rating ?: 0f
                                PreferenceSetupAnimeItem(
                                    anime = anime,
                                    committedRating = committedRating,
                                    onSaveRating = { rating -> onAction(PreferenceSetupAction.OnSaveRatingClick(index, rating)) },
                                    onCancelRating = { onAction(PreferenceSetupAction.OnCancelRatingClick(index)) },
                                )
                            }
                        }
                        if (state.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(
                                        color = AniPickTheme.colors.primary,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = AniPickTheme.colors.backgroundGray
                )
                AniPickButton(
                    text = "완료",
                    onClick = { onAction(PreferenceSetupAction.OnCompleteClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    enabled = state.isCompleteEnabled,
                    isLoading = state.isCompleting,
                )
            }
        }
    }

    state.activeFilterSheet?.let { filterType ->
        AniPickAnimeFilterBottomSheet(
            years = state.metadata.seasonYears,
            seasons = state.metadata.seasons,
            genres = state.metadata.genres,
            types = state.metadata.types,
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

private fun FilterType.toAnimeFilterTab(): AnimeFilterTab = when (this) {
    FilterType.YEAR, FilterType.SEASON -> AnimeFilterTab.YEAR_SEASON
    FilterType.GENRE -> AnimeFilterTab.GENRE
    FilterType.TYPE -> AnimeFilterTab.TYPE
}

@Composable
private fun PreferenceSetupAnimeItem(
    anime: Anime,
    committedRating: Float,
    onSaveRating: (Float) -> Unit,
    onCancelRating: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var draftRating by rememberSaveable { mutableFloatStateOf(committedRating) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
                .clickable(onClick = { isExpanded = !isExpanded })
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.coverImageUrl.ifBlank { null } ?: R.drawable.default_image_preference,
                contentDescription = "애니메이션 커버 이미지",
                modifier = Modifier
                    .size(width = 132.dp, height = 88.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = anime.title,
                        color = AniPickTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = AniPickTheme.typography.body2,
                        modifier = Modifier
                            .weight(1f)
                    )
                    if (committedRating != 0f) {
                        PreferenceSetupCompactButton(
                            onClick = { onCancelRating() },
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    maxLines = 1,
                ) {
                    anime.genres.forEach { genre ->
                        Text(
                            text = genre,
                            style = AniPickTheme.typography.caption2,
                            color = AniPickTheme.colors.primary,
                            modifier = Modifier
                                .background(AniPickTheme.colors.primary10, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                if (committedRating != 0f) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AniPickStarRatingBar(
                            rating = committedRating,
                            onRatingChange = {},
                            enabled = false,
                            starSize = 20.dp,
                            spacing = 0.dp,
                        )
                        Text(
                            text = "($committedRating)",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AniPickStarRatingBar(
                        rating = draftRating,
                        onRatingChange = { draftRating = it },
                        starSize = 28.dp,
                        spacing = 0.dp,
                    )
                    Text(
                        text = "($draftRating/5.0)",
                        style = AniPickTheme.typography.body1,
                        color = AniPickTheme.colors.point,
                    )
                }
                AniPickButton(
                    text = "평가하기",
                    onClick = {
                        onSaveRating(draftRating)
                        isExpanded = false
                    },
                    size = ButtonSize.S,
                )
            }
        }
    }
}

@Composable
private fun PreferenceSetupAnimeItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AniPickShimmerBox(
            modifier = Modifier.size(width = 132.dp, height = 88.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AniPickShimmerBox(
                modifier = Modifier
                    .width(160.dp)
                    .height(18.dp)
            )
            AniPickShimmerBox(
                modifier = Modifier
                    .width(100.dp)
                    .height(14.dp)
            )
        }
    }
}

@Composable
private fun PreferenceSetupCompactButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.primary)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "평가취소",
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferenceSetupAnimeItemPreview() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PreferenceSetupAnimeItem(
            anime = Anime(
                animeId = 1L,
                title = "귀멸의 칼날 asdfdafdsfdsfdsafdsafdsfads",
                coverImageUrl = "",
                genres = listOf("액션", "판타지"),
            ),
            committedRating = 0f,
            onSaveRating = {},
            onCancelRating = {},
        )
        PreferenceSetupAnimeItem(
            anime = Anime(
                animeId = 2L,
                title = "진격의 거인",
                coverImageUrl = "",
                genres = listOf("액션", "드라마", "액션", "드라마", "다크판타지"),
            ),
            committedRating = 4.5f,
            onSaveRating = {},
            onCancelRating = {},
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
