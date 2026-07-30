package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.L,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val height = when (size) {
        ButtonSize.L -> 52.dp
        ButtonSize.S -> 36.dp
    }
    val textStyle = when (size) {
        ButtonSize.L -> AniPickTheme.typography.body2
        ButtonSize.S -> AniPickTheme.typography.caption1
    }
    val indicatorSize = when (size) {
        ButtonSize.L -> 20.dp
        ButtonSize.S -> 16.dp
    }
    val contentColor = if (enabled) AniPickTheme.colors.white else AniPickTheme.colors.textGray

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) AniPickTheme.colors.primary else AniPickTheme.colors.gray)
            .clickable(
                enabled = enabled && !isLoading,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = textStyle,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AniPickButton(
            text = "로그인",
            onClick = {},
            size = ButtonSize.L,
            modifier = Modifier.fillMaxWidth()
        )
        AniPickButton(
            text = "로그인",
            onClick = {},
            size = ButtonSize.L,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        AniPickButton(
            text = "중복확인",
            onClick = {},
            size = ButtonSize.S
        )
        AniPickButton(
            text = "로그인",
            onClick = {},
            size = ButtonSize.L,
            isLoading = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
