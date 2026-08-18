package com.jparkbro.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickSectionHeader(
    title: @Composable () -> Unit,
    onMoreClick: () -> Unit,
    moreContentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 24.dp,
    iconTint: Color = AniPickTheme.colors.black,
    titlePadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(titlePadding)
            .clickable(enabled = enabled, onClick = onMoreClick)
            .semantics(mergeDescendants = true) {
                contentDescription = moreContentDescription
                role = Role.Button
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Box(modifier = Modifier.weight(1f, fill = false)) {
            title()
        }
        Icon(
            imageVector = ChevronRight,
            contentDescription = null,
            tint = if (enabled) iconTint else AniPickTheme.colors.lightGray,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun AniPickSectionHeader(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 24.dp,
    iconTint: Color = AniPickTheme.colors.black,
    titlePadding: PaddingValues = PaddingValues(),
) {
    AniPickSectionHeader(
        title = {
            Text(
                text = title,
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
        },
        onMoreClick = onMoreClick,
        moreContentDescription = "$title 더보기",
        modifier = modifier,
        enabled = enabled,
        iconSize = iconSize,
        iconTint = iconTint,
        titlePadding = titlePadding,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickSectionHeaderPreview() {
    AniPickSectionHeader(
        title = "실시간 인기",
        onMoreClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickSectionHeaderCustomIconPreview() {
    AniPickSectionHeader(
        title = "좋아요한 작품",
        onMoreClick = {},
        iconSize = 18.dp,
        iconTint = AniPickTheme.colors.textGray,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickSectionHeaderDisabledPreview() {
    AniPickSectionHeader(
        title = "좋아요한 작품",
        onMoreClick = {},
        enabled = false,
        iconSize = 18.dp,
        iconTint = AniPickTheme.colors.textGray,
    )
}
