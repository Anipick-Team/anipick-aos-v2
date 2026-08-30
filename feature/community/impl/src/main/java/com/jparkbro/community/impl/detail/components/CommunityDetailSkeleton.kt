package com.jparkbro.community.impl.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.component.AniPickShimmerBox

/** [CommunityDetailHeader]와 같은 배치의 스켈레톤 - 게시글 상세 조회(`getPostDetail`) 중에 보여준다.
 *  이미지 첨부/댓글 여부는 조회 전엔 알 수 없어서 이 둘은 스켈레톤에 넣지 않는다. */
@Composable
internal fun CommunityDetailSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.size(30.dp), shape = CircleShape)
                AniPickShimmerBox(modifier = Modifier.width(100.dp).height(14.dp))
            }
            AniPickShimmerBox(modifier = Modifier.width(80.dp).height(12.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(18.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
                AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.width(32.dp).height(12.dp))
                AniPickShimmerBox(modifier = Modifier.width(32.dp).height(12.dp))
                AniPickShimmerBox(modifier = Modifier.width(32.dp).height(12.dp))
            }
        }

        AniPickSectionDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(modifier = Modifier.width(48.dp).height(16.dp))
                AniPickShimmerBox(modifier = Modifier.width(48.dp).height(16.dp))
            }
            AniPickShimmerBox(modifier = Modifier.size(24.dp))
        }

        AniPickSectionDivider()
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityDetailSkeletonPreview() {
    CommunityDetailSkeleton()
}
