package com.jparkbro.community.impl.detail.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** [CommunityCommentItem]과 같은 배치의 스켈레톤 - 댓글 목록 조회(`getComments`) 중에 보여준다. */
@Composable
internal fun CommunityCommentItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.lightGray)
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.size(30.dp), shape = CircleShape)
                AniPickShimmerBox(modifier = Modifier.width(90.dp).height(14.dp))
            }
            AniPickShimmerBox(modifier = Modifier.width(70.dp).height(12.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AniPickShimmerBox(modifier = Modifier.width(40.dp).height(12.dp))
                AniPickShimmerBox(modifier = Modifier.width(40.dp).height(12.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityCommentItemSkeletonPreview() {
    CommunityCommentItemSkeleton()
}
