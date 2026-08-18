package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.DevicePreviews

@Composable
internal fun RowScope.WatchStatusBox(
    title: String,
    count: Int,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.backgroundGray)
            .weight(1f)
            .clickable { onAction() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black
        )
        Text(
            text = "${count}개",
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.primary
        )
    }
}

@DevicePreviews
@Composable
fun WatchStatusBoxPreview() {
    Row() {
        WatchStatusBox(
            title = "볼 애니",
            count = 2,
            onAction = {}
        )
    }
}
