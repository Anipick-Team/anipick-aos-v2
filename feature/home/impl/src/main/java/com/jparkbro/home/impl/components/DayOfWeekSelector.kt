package com.jparkbro.home.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId

private val DAYS_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")

private val MAX_ITEM_SIZE = 45.dp
private val MIN_ITEM_SIZE = 36.dp
private val ITEM_SPACING = 8.dp
private val MIN_ITEM_SPACING = 4.dp
private val MAX_FIXED_WIDTH = 420.dp

/** 한국 시간 기준 오늘 요일("월"~"일"). */
internal fun todayDayOfWeek(): String {
    val today = LocalDate.now(ZoneId.of("Asia/Seoul")).dayOfWeek
    return DAYS_OF_WEEK[today.value - 1]
}

/** 화면에 쓰는 요일("월")을 API 요일 코드(MON)로 바꾼다. */
internal fun String.toDayCode(): String = when (this) {
    "월" -> "MON"
    "화" -> "TUE"
    "수" -> "WED"
    "목" -> "THU"
    "금" -> "FRI"
    "토" -> "SAT"
    "일" -> "SUN"
    else -> error("알 수 없는 요일: $this")
}

/** 버튼 크기를 유지하고 옆으로 스크롤되는 요일 선택 - 선택된 요일이 화면 밖이면 보이는 곳까지 스크롤한다. */
@Composable
internal fun DayOfWeekScrollableSelector(
    selectedDay: String,
    onDaySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
) {
    val listState = rememberLazyListState()
    val selectedIndex = DAYS_OF_WEEK.indexOf(selectedDay)

    LaunchedEffect(selectedDay) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.isNotEmpty() }.first { it }
        val layoutInfo = listState.layoutInfo
        val item = layoutInfo.visibleItemsInfo.firstOrNull { it.index == selectedIndex }
        val isFullyVisible = item != null &&
            item.offset >= layoutInfo.viewportStartOffset &&
            item.offset + item.size <= layoutInfo.viewportEndOffset
        if (!isFullyVisible) listState.animateScrollToItem(selectedIndex)
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
    ) {
        items(DAYS_OF_WEEK) { day ->
            DayOfWeekChip(
                day = day,
                selected = day == selectedDay,
                size = MAX_ITEM_SIZE,
                onClick = { onDaySelected(day) },
            )
        }
    }
}

/** 7개 요일이 항상 한 줄에 다 보이는 요일 선택 - 좁으면 버튼이 [MIN_ITEM_SIZE]까지 줄고, 넓으면 간격으로 채운다.
 *  [MAX_FIXED_WIDTH]보다 넓어지진 않는다. */
@Composable
internal fun DayOfWeekSelector(
    selectedDay: String,
    onDaySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.widthIn(max = MAX_FIXED_WIDTH).fillMaxWidth()) {
        val itemSize = ((maxWidth - MIN_ITEM_SPACING * (DAYS_OF_WEEK.size - 1)) / DAYS_OF_WEEK.size)
            .coerceIn(MIN_ITEM_SIZE, MAX_ITEM_SIZE)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DAYS_OF_WEEK.forEach { day ->
                DayOfWeekChip(
                    day = day,
                    selected = day == selectedDay,
                    size = itemSize,
                    onClick = { onDaySelected(day) },
                )
            }
        }
    }
}

@Composable
private fun DayOfWeekChip(
    day: String,
    selected: Boolean,
    size: Dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (selected) AniPickTheme.colors.primary else AniPickTheme.colors.gray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day,
            style = AniPickTheme.typography.h3,
            color = if (selected) AniPickTheme.colors.white else AniPickTheme.colors.textGray,
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun DayOfWeekScrollableSelectorNarrowPreview() {
    DayOfWeekScrollableSelector(selectedDay = "일", onDaySelected = {})
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DayOfWeekScrollableSelectorPreview() {
    DayOfWeekScrollableSelector(selectedDay = "월", onDaySelected = {})
}

@Preview(showBackground = true, widthDp = 600)
@Composable
private fun DayOfWeekScrollableSelectorWidePreview() {
    DayOfWeekScrollableSelector(selectedDay = "수", onDaySelected = {})
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun DayOfWeekSelectorNarrowPreview() {
    DayOfWeekSelector(selectedDay = "월", onDaySelected = {}, modifier = Modifier.padding(horizontal = 20.dp))
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DayOfWeekSelectorPreview() {
    DayOfWeekSelector(selectedDay = "수", onDaySelected = {}, modifier = Modifier.padding(horizontal = 20.dp))
}

@Preview(showBackground = true, widthDp = 600)
@Composable
private fun DayOfWeekSelectorWidePreview() {
    DayOfWeekSelector(selectedDay = "일", onDaySelected = {}, modifier = Modifier.padding(horizontal = 20.dp))
}
