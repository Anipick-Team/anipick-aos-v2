package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.ChevronDown
import com.jparkbro.core.designsystem.model.AniPickDropdownMenuItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 화면 오른쪽 끝에 현재 정렬 기준(선택 라벨)을 보여주는 드롭다운 트리거. */
@Composable
fun <T> AniPickSortDropdown(
    options: List<Pair<T, String>>,
    selected: T,
    onSortSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedLabel = options.first { it.first == selected }.second

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AniPickDropdownMenuIcon(
            items = options.map { (option, label) ->
                AniPickDropdownMenuItem(
                    content = {
                        Text(
                            text = label,
                            style = AniPickTheme.typography.caption1,
                            color = if (option == selected) {
                                AniPickTheme.colors.black
                            } else {
                                AniPickTheme.colors.textGray
                            },
                        )
                    },
                    onClick = { onSortSelected(option) },
                )
            },
            trigger = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedLabel,
                        style = AniPickTheme.typography.caption1,
                        color = AniPickTheme.colors.textGray,
                    )
                    Icon(
                        imageVector = ChevronDown,
                        contentDescription = "정렬 기준 선택 아이콘",
                        tint = AniPickTheme.colors.textGray,
                        modifier = Modifier.size(16.dp),
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickSortDropdownPreview() {
    AniPickSortDropdown(
        options = listOf("latest" to "최신순", "popularity" to "인기순"),
        selected = "popularity",
        onSortSelected = {},
    )
}
