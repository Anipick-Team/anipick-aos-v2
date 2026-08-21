package com.jparkbro.auth.impl.email.signup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun AgreementCheckRow(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onToggle),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AniPickValidationCheckIcon(isValid = checked)
            Text(
                text = label,
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.black,
            )
        }
        if (onDetailClick != null) {
            Icon(
                imageVector = ChevronRight,
                contentDescription = "자세히 보기",
                tint = AniPickTheme.colors.gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onDetailClick),
            )
        }
    }
}
