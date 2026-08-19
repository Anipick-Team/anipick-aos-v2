package com.jparkbro.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.model.AniPickTabItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * Tab Content 사이즈만큼만 넓이를 차지하는 TabRow.
 */
@Composable
fun AniPickPrimaryTabRow(
    tabs: List<AniPickTabItem>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var tabBounds by remember { mutableStateOf(emptyMap<Int, Pair<Dp, Dp>>()) }

    Box(
        modifier = modifier
            .background(AniPickTheme.colors.white)
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                Row(
                    modifier = Modifier
                        .clickable { onTabClick(index) }
                        .onGloballyPositioned { coordinates ->
                            val offset = with(density) { coordinates.positionInParent().x.toDp() }
                            val width = with(density) { coordinates.size.width.toDp() }
                            tabBounds = tabBounds + (index to (offset to width))
                        },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = tab.label,
                        style = AniPickTheme.typography.body2,
                        color = if (isSelected) AniPickTheme.colors.black else AniPickTheme.colors.textGray,
                    )
                    tab.subLabel?.let { subLabel ->
                        Text(
                            text = subLabel,
                            style = AniPickTheme.typography.caption1,
                            color = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.textGray,
                        )
                    }
                }
            }
        }

        val (targetOffset, targetWidth) = tabBounds[selectedIndex] ?: (0.dp to 0.dp)
        val indicatorOffset by animateDpAsState(targetValue = targetOffset, label = "tabIndicatorOffset")
        val indicatorWidth by animateDpAsState(targetValue = targetWidth, label = "tabIndicatorWidth")

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = indicatorOffset)
                .width(indicatorWidth.coerceAtLeast(0.dp)),
            thickness = 2.dp,
            color = AniPickTheme.colors.black,
        )
    }
}

/**
 * 화면(부모) 전체 넓이를 차지하며 탭이 균등하게 나눠지는 TabRow.
 */
@Composable
fun AniPickSecondaryTabRow(
    tabs: List<AniPickTabItem>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = AniPickTheme.colors.white,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex, matchContentSize = false),
                color = AniPickTheme.colors.black,
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedIndex == index
            Tab(
                selected = isSelected,
                onClick = { onTabClick(index) },
                text = {
                    Text(
                        text = if (tab.subLabel != null) "${tab.label} ${tab.subLabel}" else tab.label,
                        style = AniPickTheme.typography.caption1,
                        color = AniPickTheme.colors.black
                    )
                },
                selectedContentColor = AniPickTheme.colors.black,
                unselectedContentColor = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickPrimaryTabRowPreview() {
    AniPickPrimaryTabRow(
        tabs = listOf(
            AniPickTabItem(label = "작품", subLabel = "12건"),
            AniPickTabItem(label = "인물", subLabel = "3건"),
            AniPickTabItem(label = "제작사", subLabel = "1건"),
        ),
        selectedIndex = 0,
        onTabClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickSecondaryTabRowPreview() {
    AniPickSecondaryTabRow(
        tabs = listOf(
            AniPickTabItem(label = "전체"),
            AniPickTabItem(label = "인기"),
            AniPickTabItem(label = "최신"),
        ),
        selectedIndex = 0,
        onTabClick = {},
    )
}
