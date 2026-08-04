package com.jparkbro.auth.impl.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** auth 화면들의 필드 에러/전역 에러 메시지에 공통으로 쓰는 텍스트. [message]가 null이면 아무 것도 그리지 않는다. */
@Composable
internal fun AuthErrorText(
    message: String?,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    if (message == null) return

    Text(
        text = message,
        style = AniPickTheme.typography.caption1,
        color = AniPickTheme.colors.point,
        textAlign = textAlign,
        modifier = modifier,
    )
}
