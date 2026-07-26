package com.jparkbro.anipick.navigation

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.UiText

@Composable
internal fun AniPickBottomNavigation(
    currentKey: NavKey,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    BOTTOM_NAV_ITEMS.forEach { (navKey, item) ->
        val isSelected = currentKey::class == navKey::class

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
                color = AniPickTheme.colors.gray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AniPickBottomNavigationItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onNavigate(navKey) }
                )
            }
        }
    }
}

@Composable
fun AniPickBottomNavigationItem(
    item: TopLevelNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.textGray

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
            color = if (isSelected) AniPickTheme.colors.primary else Color.Transparent
        )
        Spacer(modifier = Modifier.height(12.dp))
        Icon(
            imageVector = ImageVector.vectorResource(if (isSelected) item.selectedIcon else item.unselectedIcon),
            contentDescription = UiText.StringResource(item.iconTextId).asString(),
            tint = contentColor,
            modifier = Modifier.width(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = UiText.StringResource(item.iconTextId).asString(),
            style = AniPickTheme.typography.caption1,
            color = contentColor
        )
    }
}