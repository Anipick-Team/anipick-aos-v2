package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 얇은 회색 선(1dp) + 그 아래 두꺼운 연회색 띠(3dp)로 이루어진 섹션 구분선. */
@Composable
fun AniPickSectionDivider(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        HorizontalDivider(
            thickness = 1.dp,
            color = AniPickTheme.colors.gray,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(AniPickTheme.colors.lightGray)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickSectionDividerPreview() {
    AniPickSectionDivider()
}
