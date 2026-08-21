package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Edit
import com.jparkbro.core.designsystem.icon.MoreVertical
import com.jparkbro.core.designsystem.model.AniPickDropdownMenuItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 트리거 + 드롭다운 메뉴를 묶은 공통 컴포넌트. */
@Composable
fun AniPickDropdownMenuIcon(
    items: List<AniPickDropdownMenuItem>,
    modifier: Modifier = Modifier,
    trigger: @Composable () -> Unit = {
        Icon(
            imageVector = MoreVertical,
            contentDescription = "더보기 아이콘",
            tint = AniPickTheme.colors.textGray,
            modifier = Modifier.size(20.dp),
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(modifier = Modifier.clickable { expanded = true }) {
            trigger()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = AniPickTheme.colors.white,
            modifier = Modifier.widthIn(min = 84.dp),
            offset = DpOffset(x = 0.dp, y = 8.dp),
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = false
                            item.onClick()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    item.content()
                }
                if (index != items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp),
                        color = AniPickTheme.colors.lightGray,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickDropdownMenuIconPreview() {
    AniPickDropdownMenuIcon(
        items = listOf(
            AniPickDropdownMenuItem(
                content = {
                    Text(
                        text = "수정",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.black,
                    )
                },
                onClick = {},
            ),
            AniPickDropdownMenuItem(
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Edit,
                            contentDescription = null,
                            tint = AniPickTheme.colors.point,
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = "삭제",
                            style = AniPickTheme.typography.body2,
                            color = AniPickTheme.colors.point,
                        )
                    }
                },
                onClick = {},
            ),
        ),
    )
}
