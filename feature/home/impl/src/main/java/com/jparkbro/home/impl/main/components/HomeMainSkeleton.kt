package com.jparkbro.home.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.ui.component.AniPickAnimeCardSkeleton

@Composable
internal fun MainScreenSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState(), enabled = false)
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
            userScrollEnabled = false,
        ) {
            items(10) {
                AniPickAnimeCardSkeleton(cardWidth = 128.dp)
            }
        }
    }
}
