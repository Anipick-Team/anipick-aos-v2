package com.jparkbro.home.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.component.AniPickSectionHeader

/** [title]을 String이 아니라 컴포저블 슬롯으로 받는 오버로드 - 제목 일부만 색이 다른 섹션용 */
@Composable
internal fun HomeSection(
    title: @Composable () -> Unit,
    onMoreClick: () -> Unit,
    moreContentDescription: String,
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(),
    subContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickSectionHeader(
            title = title,
            onMoreClick = onMoreClick,
            moreContentDescription = moreContentDescription,
            titlePadding = titlePadding,
        )

        subContent?.invoke()

        content()
    }
}

/** 제목이 단색 텍스트 하나뿐인 대부분의 섹션을 위한 편의 오버로드. */
@Composable
internal fun HomeSection(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    titlePadding: PaddingValues = PaddingValues(),
    subContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    HomeSection(
        title = {
            Text(
                text = title,
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
        },
        onMoreClick = onMoreClick,
        moreContentDescription = "$title 더보기",
        modifier = modifier,
        titlePadding = titlePadding,
        subContent = subContent,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeSectionTitleOnlyPreview() {
    HomeSection(
        title = "인기 급상승",
        onMoreClick = {},
    ) {
        Text(
            text = "content slot (LazyRow 등)",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSectionWithSubContentPreview() {
    HomeSection(
        title = "요일별 신작",
        onMoreClick = {},
        subContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("월", style = AniPickTheme.typography.caption1)
                Text("화", style = AniPickTheme.typography.caption1)
                Text("수", style = AniPickTheme.typography.caption1)
            }
        },
    ) {
        Text(
            text = "content slot (LazyRow 등)",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSectionWithComposableTitlePreview() {
    HomeSection(
        title = {
            Row {
                Text(
                    text = "jparkbro",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.primary,
                )
                Text(
                    text = "님을 위한 추천",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.black,
                )
            }
        },
        onMoreClick = {},
        moreContentDescription = "추천 더보기",
    ) {
        Text(
            text = "content slot (LazyRow 등)",
        )
    }
}
