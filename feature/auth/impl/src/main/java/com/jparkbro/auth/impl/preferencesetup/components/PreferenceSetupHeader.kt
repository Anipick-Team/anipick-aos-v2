package com.jparkbro.auth.impl.preferencesetup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun PreferenceSetupHeader(
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "건너뛰기",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onSkipClick() }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "좋아하는 애니메이션을 편가해주세요.\n취향에 맞는 작품을 추천할게요.",
            style = AniPickTheme.typography.h1,
            color = AniPickTheme.colors.black,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "좋아하는 애니메이션을 골라 주세요.",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.primary,
        )
    }
}
