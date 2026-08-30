package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.icon.ChevronLeft
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 축소 상태에서 sticky header에 얹는 "뒤로가기 + 제목 + 찜" 바 */
@Composable
internal fun AnimeCollapsedHeaderBar(
    title: String,
    isLiked: Boolean,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .height(60.dp)
            .padding(horizontal = 20.dp),
    ) {
        Icon(
            imageVector = ChevronLeft,
            contentDescription = "뒤로가기",
            tint = AniPickTheme.colors.black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick),
        )
        Text(
            text = title,
            style = AniPickTheme.typography.h3,
            color = AniPickTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 40.dp),
        )
        AniPickAnimatedHeartIcon(
            isLiked = isLiked,
            onClick = onLikeClick,
            contentDescription = "찜하기",
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AnimeCollapsedHeaderBarPreview() {
    AnimeCollapsedHeaderBar(
        title = "장송의 프리렌 2기",
        isLiked = true,
        onBackClick = {},
        onLikeClick = {},
    )
}
