package com.jparkbro.mypage.impl.setting.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun SettingItemRow(
    modifier: Modifier = Modifier,
    label: String,
    labelColor: Color = AniPickTheme.colors.black,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled, onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = AniPickTheme.typography.body1,
            color = labelColor
        )
        trailingContent?.invoke()
    }
}
