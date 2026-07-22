package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.BottomNav1Selected
import com.jparkbro.core.designsystem.icon.BottomNav1Unselected
import com.jparkbro.core.designsystem.icon.BottomNav2Selected
import com.jparkbro.core.designsystem.icon.BottomNav2Unselected
import com.jparkbro.core.designsystem.icon.BottomNav3Selected
import com.jparkbro.core.designsystem.icon.BottomNav3Unselected
import com.jparkbro.core.designsystem.icon.BottomNav4Selected
import com.jparkbro.core.designsystem.icon.BottomNav4Unselected
import com.jparkbro.core.designsystem.model.BottomNavigationItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickBottomNavigation(
    items: List<BottomNavigationItem>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .height(78.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = AniPickTheme.colors.gray2
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEachIndexed { index, item ->
                AniPickBottomNavigationItem(
                    item = item,
                    selected = index == selectedIndex,
                    onClick = { onItemClick(index) }
                )
            }
        }
    }
}

@Composable
fun AniPickBottomNavigationItem(
    item: BottomNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) AniPickTheme.colors.primary else AniPickTheme.colors.textGray

    Column(
        modifier = modifier
            .width(60.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp,
            color = if (selected) AniPickTheme.colors.primary else Color.Transparent
        )
        Spacer(modifier = Modifier.height(12.dp))
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.width(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.label,
            style = AniPickTheme.typography.caption1,
            color = contentColor
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AniPickBottomNavigationPreview() {
    val items = listOf(
        BottomNavigationItem("홈", BottomNav1Selected, BottomNav1Unselected),
        BottomNavigationItem("탐색", BottomNav2Selected, BottomNav2Unselected),
        BottomNavigationItem("찜", BottomNav3Selected, BottomNav3Unselected),
        BottomNavigationItem("MY", BottomNav4Selected, BottomNav4Unselected),
    )
    AniPickBottomNavigation(
        items = items,
        selectedIndex = 0,
        onItemClick = {}
    )
}