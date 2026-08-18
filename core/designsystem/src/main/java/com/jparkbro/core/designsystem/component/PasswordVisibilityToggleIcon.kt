package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jparkbro.core.designsystem.icon.VisibilityOff
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickPasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = if (showPassword) VisibilityOn else VisibilityOff,
        contentDescription = "비밀번호 표시 전환",
        tint = AniPickTheme.colors.textGray,
        modifier = modifier.clickable(onClick = onToggle),
    )
}
