package com.jparkbro.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickWheelPicker
import com.jparkbro.core.designsystem.component.AniPickWheelPickerDefaultItem
import com.jparkbro.core.designsystem.theme.AniPickTheme

internal val WHEEL_ITEM_WIDTH = 88.dp
internal val WHEEL_ITEM_HEIGHT = 36.dp

/**
 * [AniPickWheelPicker]에 선택 영역을 primary 색 테두리 박스로, 선택된 아이템 텍스트를 h3/primary로
 * 꾸민 커스텀 스타일. 이 바텀시트(년도/분기 등)에서만 쓰는 스타일이라 core:designsystem이 아니라
 * core:ui에 둔다.
 */
@Composable
fun <T> AniPickStyledWheelPicker(
    items: List<T>,
    selectedItem: T,
    onSelectedItemChange: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    itemHeight: Dp = WHEEL_ITEM_HEIGHT,
    backgroundColor: Color = Color.Transparent,
    resetTrigger: Int = 0,
    enabled: Boolean = true,
    onCenteredItemChange: (T) -> Unit = {},
) {
    AniPickWheelPicker(
        items = items,
        selectedItem = selectedItem,
        onSelectedItemChange = onSelectedItemChange,
        itemLabel = itemLabel,
        modifier = modifier
            .width(WHEEL_ITEM_WIDTH),
        itemHeight = itemHeight,
        backgroundColor = backgroundColor,
        resetTrigger = resetTrigger,
        enabled = enabled,
        onCenteredItemChange = onCenteredItemChange,
        itemContent = { _, label, fraction ->
            AniPickWheelPickerDefaultItem(
                label = label,
                fraction = fraction,
                selectedColor = if (enabled) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
            )
        },
        selectionIndicator = { WheelSelectionIndicator(itemHeight, enabled) },
    )
}

@Composable
private fun BoxScope.WheelSelectionIndicator(itemHeight: Dp, enabled: Boolean) {
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(itemHeight)
            .border(
                2.dp,
                if (enabled) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                RoundedCornerShape(8.dp),
            ),
    )
}
