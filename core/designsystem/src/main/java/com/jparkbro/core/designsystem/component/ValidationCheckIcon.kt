package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Check
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickValidationCheckIcon(
    isValid: Boolean,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Check,
        contentDescription = "체크",
        tint = if (isValid) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
        modifier = modifier.size(24.dp),
    )
}
