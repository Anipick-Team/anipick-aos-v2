package com.jparkbro.explore.impl.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.core.designsystem.component.AniPickRemovableChip

@Composable
internal fun RemovableFilterChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AniPickRemovableChip(
        text = text,
        onRemove = onRemove,
        modifier = modifier,
        removeContentDescription = "$text 필터 해제",
    )
}

@Preview(showBackground = true)
@Composable
private fun RemovableFilterChipPreview() {
    RemovableFilterChip(
        text = "2024",
        onRemove = {},
    )
}
