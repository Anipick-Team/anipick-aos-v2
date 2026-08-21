package com.jparkbro.core.ui.component

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

/** 옵션 선택 칩 리스트
 *  [allowMultiSelect] false: 단일 선택, true: 다중 선택 + "모든 조건 일치"([matchAll]) 토글 표시 */
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
