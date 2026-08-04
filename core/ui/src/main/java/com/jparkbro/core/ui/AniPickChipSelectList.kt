package com.jparkbro.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSelectChip

/**
 * 옵션 리스트에서 하나만 고르는 칩 형태의 단일 선택 리스트. 여러 줄로 자동 줄바꿈되고, 넘치면 세로로 스크롤된다.
 * 호출부에서 장르/타입 같은 metadata(id, label) 항목을 그대로 넘길 걸 감안해서 core:ui에 둔다.
 */
@Composable
fun <T> AniPickChipSelectList(
    options: List<T>,
    selectedOption: T?,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            AniPickSelectChip(
                text = optionLabel(option),
                isSelected = option == selectedOption,
                onClick = { onSelect(option) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickChipSelectListPreview() {
    AniPickChipSelectList(
        options = listOf("액션", "판타지", "로맨스", "코미디", "드라마"),
        selectedOption = "판타지",
        optionLabel = { it },
        onSelect = {},
    )
}
