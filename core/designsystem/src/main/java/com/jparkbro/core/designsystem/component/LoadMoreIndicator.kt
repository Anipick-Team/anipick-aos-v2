package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickLoadMoreIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = AniPickTheme.colors.primary)
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickLoadMoreIndicatorPreview() {
    AniPickLoadMoreIndicator()
}
