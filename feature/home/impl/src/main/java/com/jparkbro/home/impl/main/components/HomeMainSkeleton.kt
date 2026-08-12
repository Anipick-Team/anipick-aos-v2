package com.jparkbro.home.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickShimmerBox

/** 홈 메인 화면이 로딩 중일 때 [HomeSection] 대신 보여주는 자리표시자. 실제 섹션 개수만큼
 *  반복하지 않고, 화면 진입 즉시 보이는 만큼만 반복해서 스크롤 없이도 로딩감을 준다. */
@Composable
internal fun MainScreenSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(100.dp),
    ) {
        repeat(6) {
            HomeSectionSkeleton()
        }
    }
}

@Composable
private fun HomeSectionSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickShimmerBox(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .width(140.dp)
                .height(20.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(3) {
                AnimeCardSkeleton()
            }
        }
    }
}

/** 애니 카드와 같은 비율(128:182)의 이미지 자리 + 제목 한 줄 자리로 카드 모양을 흉내낸다. */
@Composable
private fun AnimeCardSkeleton(
    cardWidth: Dp = 128.dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(cardWidth),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AniPickShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(128f / 182f),
        )
        AniPickShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(16.dp),
        )
    }
}
