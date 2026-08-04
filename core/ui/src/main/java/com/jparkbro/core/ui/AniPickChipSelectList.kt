package com.jparkbro.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSelectChip
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * 옵션 리스트에서 고르는 칩 형태의 선택 리스트. 여러 줄로 자동 줄바꿈되고, 넘치면 세로로 스크롤된다.
 * 호출부에서 장르/타입 같은 metadata(id, label) 항목을 그대로 넘길 걸 감안해서 core:ui에 둔다.
 *
 * [allowMultiSelect]가 false면(기본값) 칩 하나만 고를 수 있고 "모든 조건 일치" 토글은 숨겨진다.
 * true면 여러 칩을 동시에 고를 수 있고, 그 선택들을 AND로 매칭할지([matchAll])를 정하는 토글이
 * FlowRow 위쪽에 나타난다.
 */
@Composable
fun <T> AniPickChipSelectList(
    options: List<T>,
    selectedOptions: List<T>,
    optionLabel: (T) -> String,
    onSelectionChange: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    allowMultiSelect: Boolean = false,
    matchAll: Boolean = false,
    onMatchAllChange: (Boolean) -> Unit = {},
) {
    Column(modifier = modifier) {
        if (allowMultiSelect) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "모든 조건 일치",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.black,
                )
                AniPickSwitch(
                    checked = matchAll,
                    onCheckedChange = onMatchAllChange,
                )
            }
        }
        FlowRow(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { option ->
                val isSelected = option in selectedOptions
                AniPickSelectChip(
                    text = optionLabel(option),
                    isSelected = isSelected,
                    onClick = {
                        val newSelection = if (allowMultiSelect) {
                            if (isSelected) selectedOptions - option else selectedOptions + option
                        } else {
                            listOf(option)
                        }
                        onSelectionChange(newSelection)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickChipSelectListSingleSelectPreview() {
    AniPickChipSelectList(
        options = listOf("액션", "판타지", "로맨스", "코미디", "드라마"),
        selectedOptions = listOf("판타지"),
        optionLabel = { it },
        onSelectionChange = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickChipSelectListMultiSelectPreview() {
    AniPickChipSelectList(
        options = listOf("액션", "판타지", "로맨스", "코미디", "드라마"),
        selectedOptions = listOf("판타지", "액션"),
        optionLabel = { it },
        onSelectionChange = {},
        allowMultiSelect = true,
        matchAll = true,
        onMatchAllChange = {},
    )
}
