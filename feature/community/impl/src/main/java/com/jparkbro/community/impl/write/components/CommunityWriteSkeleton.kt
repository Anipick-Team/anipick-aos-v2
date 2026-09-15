package com.jparkbro.community.impl.write.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.community.impl.write.MAX_IMAGE_COUNT
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** [communityWriteFields]와 같은 배치의 스켈레톤 - 글 수정 진입 시 기존 글 조회(`getPostDetail`) 중에 보여준다.
 *  라벨/안내 문구는 실제 텍스트 그대로 두고, 입력 필드/스위치/사진 자리만 shimmer로 가린다. */
@Composable
internal fun CommunityWriteSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AniPickLabeledField(
            label = "제목",
            textField = { AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(48.dp)) },
        )
        AniPickLabeledField(
            label = "내용",
            textField = { AniPickShimmerBox(modifier = Modifier.fillMaxWidth().height(220.dp)) },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "스포일러",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.primary,
            )
            AniPickShimmerBox(modifier = Modifier.size(32.dp, 16.dp), shape = RoundedCornerShape(8.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) {
                    AniPickShimmerBox(modifier = Modifier.size(112.dp))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "• 사진은 최대 ${MAX_IMAGE_COUNT}장까지 등록 가능합니다.",
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
                Text(
                    text = "• 사진은 장당 10MB까지 첨부할 수 있어요.",
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityWriteSkeletonPreview() {
    CommunityWriteSkeleton()
}
