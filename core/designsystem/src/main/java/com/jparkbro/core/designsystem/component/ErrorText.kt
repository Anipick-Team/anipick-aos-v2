package com.jparkbro.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickErrorText(
    message: String?,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    if (message != null) {
        Text(
            text = message,
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.point,
            textAlign = textAlign,
            modifier = modifier,
        )
    }
}
