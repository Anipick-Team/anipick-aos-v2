package com.jparkbro.auth.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jparkbro.core.designsystem.icon.VisibilityOff
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 비밀번호 입력 필드의 표시/숨기기 토글 아이콘. 텍스트 필드의 trailing action 자리에 들어간다. */
@Composable
internal fun PasswordVisibilityToggleIcon(
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
