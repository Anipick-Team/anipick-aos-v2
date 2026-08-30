package com.jparkbro.community.impl.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** [CommunityPostItem]과 같은 배치의 스켈레톤 - 게시글 목록 첫 로딩 중에 보여준다. */
@Composable
internal fun CommunityPostItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.size(30.dp), shape = CircleShape)
                AniPickShimmerBox(modifier = Modifier.width(80.dp).height(14.dp))
            }
            AniPickShimmerBox(modifier = Modifier.width(60.dp).height(12.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.8f).height(16.dp))
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp))
            }
            AniPickShimmerBox(modifier = Modifier.size(112.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.width(40.dp).height(12.dp))
            AniPickShimmerBox(modifier = Modifier.width(40.dp).height(12.dp))
            AniPickShimmerBox(modifier = Modifier.width(40.dp).height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityPostItemSkeletonPreview() {
    CommunityPostItemSkeleton()
}
