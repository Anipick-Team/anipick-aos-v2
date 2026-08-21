package com.jparkbro.auth.impl.preferencesetup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun PreferenceSetupCompleteBar(
    enabled: Boolean,
    isLoading: Boolean,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = AniPickTheme.colors.backgroundGray
        )
        AniPickButton(
            text = "완료",
            onClick = onCompleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            enabled = enabled,
            isLoading = isLoading,
        )
    }
}
