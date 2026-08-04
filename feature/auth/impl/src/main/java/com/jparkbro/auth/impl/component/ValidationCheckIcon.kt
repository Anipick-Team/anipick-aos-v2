package com.jparkbro.auth.impl.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Check
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 유효성/동의 여부를 체크 아이콘 색상으로 보여준다 — [isValid]가 true면 primary, 아니면 gray. */
@Composable
internal fun ValidationCheckIcon(
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
