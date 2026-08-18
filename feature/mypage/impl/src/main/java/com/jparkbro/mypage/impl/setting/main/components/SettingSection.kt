package com.jparkbro.mypage.impl.setting.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun SettingSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        Text(
            text = title,
            style = AniPickTheme.typography.h2,
            color = AniPickTheme.colors.black
        )
        Column(content = content)
    }
}
