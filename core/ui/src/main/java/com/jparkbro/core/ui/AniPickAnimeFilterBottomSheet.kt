package com.jparkbro.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Season

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniPickAnimeFilterBottomSheet(
    years: List<Int>,
    seasons: List<Season>,
    genres: List<Genre>,
    types: List<String>,
    initialYear: Int?,
    initialSeason: Season?,
    initialGenre: Genre?,
    initialType: String?,
    onConfirm: (year: Int?, season: Season?, genre: Genre?, type: String?) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: AnimeFilterTab = AnimeFilterTab.YEAR_SEASON,
    showYearSeasonTab: Boolean = true,
    showGenreTab: Boolean = true,
    showTypeTab: Boolean = true,
    sheetHeight: Dp = 360.dp,
    sheetBackgroundColor: Color = AniPickTheme.colors.white
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        containerColor = Color.Transparent,
    ) {
        AniPickAnimeFilterBottomSheetContent(
            years = years,
            seasons = seasons,
            genres = genres,
            types = types,
            initialYear = initialYear,
            initialSeason = initialSeason,
            initialGenre = initialGenre,
            initialType = initialType,
            initialTab = initialTab,
            showYearSeasonTab = showYearSeasonTab,
            showGenreTab = showGenreTab,
            showTypeTab = showTypeTab,
            sheetHeight = sheetHeight,
            sheetBackgroundColor = sheetBackgroundColor,
            onConfirm = onConfirm,
            onDismissRequest = onDismissRequest,
        )
    }
}

/**
 * [ModalBottomSheet]는 별도의 시스템 윈도우로 떠서 Compose Preview에 내용이 렌더링되지 않는다.
 * 그래서 실제 콘텐츠를 이 컴포저블로 분리해서, Preview에서는 [ModalBottomSheet] 없이 이 함수를 직접 호출한다.
 */
@Composable
private fun AniPickAnimeFilterBottomSheetContent(
    years: List<Int>,
    seasons: List<Season>,
    genres: List<Genre>,
    types: List<String>,
    initialYear: Int?,
    initialSeason: Season?,
    initialGenre: Genre?,
    initialType: String?,
    initialTab: AnimeFilterTab,
    showYearSeasonTab: Boolean,
    showGenreTab: Boolean,
    showTypeTab: Boolean,
    sheetHeight: Dp,
    sheetBackgroundColor: Color,
    onConfirm: (year: Int?, season: Season?, genre: Genre?, type: String?) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val availableTabs = remember(showYearSeasonTab, showGenreTab, showTypeTab) {
        buildList {
            if (showYearSeasonTab) add(AnimeFilterTab.YEAR_SEASON)
            if (showGenreTab) add(AnimeFilterTab.GENRE)
            if (showTypeTab) add(AnimeFilterTab.TYPE)
        }.ifEmpty { listOf(AnimeFilterTab.YEAR_SEASON) }
    }
    var selectedTab by remember(availableTabs) {
        mutableStateOf(initialTab.takeIf { it in availableTabs } ?: availableTabs.first())
    }
    var draftYear by remember { mutableStateOf(initialYear) }
    var draftSeason by remember { mutableStateOf(initialSeason) }
    var draftGenre by remember { mutableStateOf(initialGenre) }
    var draftType by remember { mutableStateOf(initialType) }
    var resetTrigger by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .background(sheetBackgroundColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                availableTabs.forEach { tab ->
                    val isSelected = tab == selectedTab
                    Box(
                        modifier = Modifier
                            .clickable { selectedTab = tab }
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = tab.label,
                            style = AniPickTheme.typography.body2,
                            color = if (isSelected) AniPickTheme.colors.black else AniPickTheme.colors.textGray,
                        )
                    }
                }
            }
            Icon(
                imageVector = Close,
                contentDescription = "닫기",
                tint = AniPickTheme.colors.black,
                modifier = Modifier
                    .clickable(onClick = onDismissRequest)
                    .size(24.dp),
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = AniPickTheme.colors.lightGray,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when (selectedTab) {
                AnimeFilterTab.YEAR_SEASON -> YearSeasonTabContent(
                    years = years,
                    seasons = seasons,
                    year = draftYear,
                    season = draftSeason,
                    onYearChange = { draftYear = it },
                    onSeasonChange = { draftSeason = it },
                    wheelBackgroundColor = sheetBackgroundColor,
                    resetTrigger = resetTrigger,
                )

                AnimeFilterTab.GENRE -> AniPickChipSelectList(
                    options = genres,
                    selectedOptions = listOfNotNull(draftGenre),
                    optionLabel = { it.name },
                    onSelectionChange = { draftGenre = it.firstOrNull() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                )

                AnimeFilterTab.TYPE -> AniPickChipSelectList(
                    options = types,
                    selectedOptions = listOfNotNull(draftType),
                    optionLabel = { it },
                    onSelectionChange = { draftType = it.firstOrNull() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        ) {
            AniPickButton(
                text = "초기화",
                onClick = {
                    draftYear = null
                    draftSeason = null
                    draftGenre = null
                    draftType = null
                    resetTrigger++
                },
                size = ButtonSize.S,
                backgroundColor = AniPickTheme.colors.gray,
                contentColor = AniPickTheme.colors.textGray,
            )
            AniPickButton(
                text = "확인",
                onClick = {
                    val confirmedSeason = if (draftYear == null) null else draftSeason
                    onConfirm(draftYear, confirmedSeason, draftGenre, draftType)
                },
                size = ButtonSize.S,
            )
        }
    }
}

/**
 * 년도는 오름차순(과거 -> 최신)으로 두고 맨 아래에 "전체년도"(id=null)를 덧붙인다.
 * 분기는 반대로 맨 위에 "전체분기"(id=null)를 두고, 그 아래로 1~4분기가 이어진다.
 */
@Composable
private fun YearSeasonTabContent(
    years: List<Int>,
    seasons: List<Season>,
    year: Int?,
    season: Season?,
    onYearChange: (Int?) -> Unit,
    onSeasonChange: (Season?) -> Unit,
    wheelBackgroundColor: Color,
    resetTrigger: Int,
) {
    val yearItems = remember(years) { years.sorted() + listOf(null) }
    val seasonItems = remember(seasons) { listOf(null) + seasons }
    var liveYear by remember(year) { mutableStateOf(year) }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AniPickStyledWheelPicker(
            items = yearItems,
            selectedItem = year,
            onSelectedItemChange = onYearChange,
            onCenteredItemChange = { liveYear = it },
            itemLabel = { if (it == null) "전체년도" else "${it}년" },
            backgroundColor = wheelBackgroundColor,
            resetTrigger = resetTrigger,
        )
        AniPickStyledWheelPicker(
            items = seasonItems,
            selectedItem = season,
            onSelectedItemChange = onSeasonChange,
            itemLabel = { it?.name ?: "전체분기" },
            backgroundColor = wheelBackgroundColor,
            resetTrigger = resetTrigger,
            enabled = liveYear != null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickAnimeFilterBottomSheetContentPreview() {
    AniPickAnimeFilterBottomSheetContent(
        years = (2015..2026).toList().reversed(),
        seasons = listOf(
            Season(id = 1, name = "1분기"),
            Season(id = 2, name = "2분기"),
            Season(id = 3, name = "3분기"),
            Season(id = 4, name = "4분기"),
        ),
        genres = listOf(
            Genre(id = 1, name = "액션"),
            Genre(id = 2, name = "판타지"),
            Genre(id = 3, name = "로맨스"),
        ),
        types = listOf("TV", "영화", "OVA"),
        initialYear = 2024,
        initialSeason = Season(id = 2, name = "2분기"),
        initialGenre = null,
        initialType = null,
        initialTab = AnimeFilterTab.YEAR_SEASON,
        showYearSeasonTab = true,
        showGenreTab = true,
        showTypeTab = true,
        sheetHeight = 360.dp,
        sheetBackgroundColor = AniPickTheme.colors.white,
        onConfirm = { _, _, _, _ -> },
        onDismissRequest = {},
    )
}
