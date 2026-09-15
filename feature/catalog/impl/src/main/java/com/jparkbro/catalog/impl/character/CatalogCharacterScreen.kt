package com.jparkbro.catalog.impl.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.extension.modifier.BottomEdgeShadowClearance
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.character.Character
import com.jparkbro.core.ui.component.AniPickCastPairCard
import com.jparkbro.core.ui.component.AniPickCastPairCardSkeleton
import com.jparkbro.core.ui.component.calculateAnimeGridLayout
import com.jparkbro.core.ui.effect.LoadMoreEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val SKELETON_ITEM_COUNT = 8

/** 카드 한 칸(캐릭터+성우 이미지 2장) 너비 범위 - 열 개수 2~3 */
private val MIN_CELL_WIDTH = 150.dp
private val MAX_CELL_WIDTH = 200.dp
private const val MIN_COLUMNS = 2
private const val MAX_COLUMNS = 3
private val MIN_SPACING = 4.dp
private val MAX_SPACING = 16.dp
private val GRID_CONTENT_PADDING = PaddingValues(20.dp)

@Composable
internal fun CatalogCharacterRoot(
    animeId: Long,
    onBackClick: () -> Unit,
    onNavigateToActorDetail: (Long) -> Unit,
    viewModel: CatalogCharacterViewModel = koinViewModel(parameters = { parametersOf(animeId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogCharacterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogCharacterAction.Navigation -> when (action) {
                    CatalogCharacterAction.OnBackClick -> onBackClick()
                    is CatalogCharacterAction.OnCastClick -> onNavigateToActorDetail(action.personId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CatalogCharacterScreen(
    state: CatalogCharacterState,
    onAction: (CatalogCharacterAction) -> Unit,
) {
    val gridState = rememberLazyGridState()

    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "캐릭터 / 성우진",
                onBackClick = { onAction(CatalogCharacterAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = BottomEdgeShadowClearance),
        ) {
            val layoutDirection = LocalLayoutDirection.current
            val horizontalPadding = GRID_CONTENT_PADDING.calculateStartPadding(layoutDirection) +
                GRID_CONTENT_PADDING.calculateEndPadding(layoutDirection)
            val layout = remember(maxWidth) {
                calculateAnimeGridLayout(
                    availableWidth = maxWidth - horizontalPadding,
                    minCardWidth = MIN_CELL_WIDTH,
                    maxCardWidth = MAX_CELL_WIDTH,
                    minColumns = MIN_COLUMNS,
                    maxColumns = MAX_COLUMNS,
                    minHorizontalSpacing = MIN_SPACING,
                    maxHorizontalSpacing = MAX_SPACING,
                )
            }
            // AniPickCastPairCard는 캐릭터/성우 이미지 2장을 6dp 간격으로 붙인 폭을 cardWidth로 받는다.
            val pairCardWidth = (layout.cardWidth - 6.dp) / 2

            LoadMoreEffect(state = gridState, threshold = layout.columns + 1, onLoadMore = { onAction(CatalogCharacterAction.OnLoadMore) })

            LazyVerticalGrid(
                columns = GridCells.Fixed(layout.columns),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = GRID_CONTENT_PADDING,
                horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (state.isLoading) {
                    items(SKELETON_ITEM_COUNT) {
                        // wrapContentWidth로 그리드엔 셀 너비 그대로, 내부는 실제 크기로 그림
                        AniPickCastPairCardSkeleton(
                            cardWidth = pairCardWidth,
                            modifier = Modifier.wrapContentWidth(Alignment.Start),
                        )
                    }
                } else {
                    itemsIndexed(
                        state.characters,
                        key = { index, item -> "$index-${item.character?.characterId}" },
                    ) { _, animeCharacter ->
                        AniPickCastPairCard(
                            animeCharacter = animeCharacter,
                            cardWidth = pairCardWidth,
                            modifier = Modifier.wrapContentWidth(Alignment.Start),
                            onClick = {
                                animeCharacter.voiceActor?.personId?.let { personId ->
                                    onAction(CatalogCharacterAction.OnCastClick(personId))
                                }
                            },
                        )
                    }
                    if (state.isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) { AniPickLoadMoreIndicator() }
                    }
                }
            }
        }
    }
}

private val PREVIEW_CHARACTERS = listOf(
    AnimeCharacter(
        character = Character(characterId = 1L, name = "엘런 예거", imageUrl = ""),
        voiceActor = Actor(personId = 1L, name = "카지 유우키", profileImage = ""),
    ),
    AnimeCharacter(
        character = Character(characterId = 2L, name = "미카사 아커만", imageUrl = ""),
        voiceActor = Actor(personId = 2L, name = "이시카와 유이", profileImage = ""),
    ),
    AnimeCharacter(
        character = Character(characterId = 3L, name = "아르민 알레르토", imageUrl = ""),
        voiceActor = null,
    ),
)

@Composable
@Preview(showBackground = true)
private fun CatalogCharacterScreenPreview() {
    CatalogCharacterScreen(
        state = CatalogCharacterState(animeId = 1L, characters = PREVIEW_CHARACTERS),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CatalogCharacterScreenSkeletonPreview() {
    CatalogCharacterScreen(
        state = CatalogCharacterState(animeId = 1L, isLoading = true),
        onAction = {},
    )
}
