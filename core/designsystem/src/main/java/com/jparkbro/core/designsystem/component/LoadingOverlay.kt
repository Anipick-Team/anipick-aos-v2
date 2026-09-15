package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 화면 전체를 어둡게 덮고 터치를 막는 로딩 오버레이 - 시간이 걸리는 등록/업로드 중 다른 조작을 막을 때 쓴다. */
@Composable
fun AniPickLoadingOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = AniPickTheme.colors.white)
    }
}

@Composable
@Preview(showBackground = true)
private fun AniPickLoadingOverlayPreview() {
    AniPickLoadingOverlay()
}
