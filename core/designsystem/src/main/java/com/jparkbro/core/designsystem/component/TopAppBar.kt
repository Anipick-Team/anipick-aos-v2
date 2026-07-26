package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.extension.modifier.bottomEdgeShadow
import com.jparkbro.core.designsystem.icon.ArrowLeft
import com.jparkbro.core.designsystem.icon.ChevronLeft
import com.jparkbro.core.designsystem.icon.Search
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickShellTopAppBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .bottomEdgeShadow()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(TopAppBarHeight)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.app_logo),
            contentDescription = "앱로고",
            tint = Color.Unspecified,
            modifier = Modifier.height(16.dp)
        )
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Search,
                contentDescription = "검색아이콘",
                tint = AniPickTheme.colors.textGray,
                modifier = Modifier.width(24.dp)
            )
        }
    }
}

@Composable
fun AniPickTitleTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: () -> Unit,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .bottomEdgeShadow()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(TopAppBarHeight)
            .padding(horizontal = 20.dp),
    ) {
        Icon(
            imageVector = ChevronLeft,
            contentDescription = "뒤로가기 아이콘",
            tint = AniPickTheme.colors.black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(
                    onClick = onBackClick
                )
        )
        title?.let { title ->
            Text(
                text = title,
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        actions?.let { actionsContent ->
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = actionsContent
            )
        }
    }
}

@Composable
fun AniPickSearchTopAppBar(
    value: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색어를 입력하세요"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .height(TopAppBarHeight)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = ArrowLeft,
                contentDescription = "뒤로가기",
                tint = AniPickTheme.colors.black
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.textGray
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = AniPickTheme.typography.body2.copy(color = AniPickTheme.colors.black),
                cursorBrush = SolidColor(AniPickTheme.colors.primary)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AniPickShellTopAppBarPreview() {
    AniPickShellTopAppBar(onSearchClick = {})
}

@Composable
@Preview(showBackground = true)
private fun AniPickSearchTopAppBarPreview() {
    AniPickSearchTopAppBar(
        value = "",
        onValueChange = {},
        onBackClick = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun AniPickTitleTopAppBarPreview() {
    AniPickTitleTopAppBar(
        title = "타이틀",
        onBackClick = {},
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Search,
                    contentDescription = "검색",
                    tint = AniPickTheme.colors.black
                )
            }
        }
    )
}

private val TopAppBarHeight = 60.dp