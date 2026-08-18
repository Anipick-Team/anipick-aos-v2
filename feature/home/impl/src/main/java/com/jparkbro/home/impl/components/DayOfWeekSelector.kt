package com.jparkbro.home.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

private val DAYS_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")
private val MAX_ITEM_SIZE = 45.dp
private val ITEM_SPACING = 8.dp

@Composable
internal fun DayOfWeekSelector(
    selectedDay: String,
    onDaySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val itemSize = minOf(
            MAX_ITEM_SIZE,
            (maxWidth - ITEM_SPACING * (DAYS_OF_WEEK.size - 1)) / DAYS_OF_WEEK.size,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING)) {
            DAYS_OF_WEEK.forEach { day ->
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .clip(CircleShape)
                        .background(if (selectedDay == day) AniPickTheme.colors.primary else AniPickTheme.colors.gray)
                        .clickable(onClick = { onDaySelected(day) }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = AniPickTheme.typography.h3,
                        color = if (selectedDay == day) AniPickTheme.colors.white else AniPickTheme.colors.textGray,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekSelectorPreview() {
    DayOfWeekSelector(
        selectedDay = "월",
        onDaySelected = {},
    )
}
