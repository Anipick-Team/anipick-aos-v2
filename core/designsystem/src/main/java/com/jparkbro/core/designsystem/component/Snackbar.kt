package com.jparkbro.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jparkbro.core.designsystem.extension.modifier.dropShadow
import com.jparkbro.core.designsystem.theme.AniPickTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/** 앱 상단에 떠서 일정 시간 후 자동으로 사라지는 커스텀 스낵바 - 퇴장 애니메이션이 끝나면 [onDismiss] 호출. */
@Composable
fun AniPickSnackbar(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    duration: Long = 2000L,
) {
    val visibleState = remember(message) {
        MutableTransitionState(false)
    }

    LaunchedEffect(message) {
        visibleState.targetState = true
        delay(duration)
        visibleState.targetState = false
        delay(300.milliseconds)
        onDismiss()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f)
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(tween(300)) + slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(300)
            ),
            exit = fadeOut(tween(300)) + slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 20.dp)
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadow(
                        blurRadius = 12.dp,
                        offsetY = 4.dp,
                        color = Color.Black.copy(alpha = 0.2f),
                        cornerRadius = 8.dp,
                    )
                    .background(AniPickTheme.colors.black, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.white,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickSnackBarPreview() {
    AniPickSnackbar(
        message = "네트워크 연결을 확인해주세요.",
        onDismiss = {},
    )
}