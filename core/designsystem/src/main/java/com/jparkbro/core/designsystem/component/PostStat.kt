package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Comment
import com.jparkbro.core.designsystem.icon.HeartOutlined
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 조회수/좋아요/댓글 수 같은 통계 하나 - 아이콘 + 숫자. 게시글 목록/상세, 마이페이지 내 콘텐츠 등에서 함께 쓴다. */
@Composable
fun AniPickPostStat(icon: ImageVector, count: Int?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AniPickTheme.colors.textGray,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = "${count ?: 0}",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.textGray,
        )
    }
}

/** [AniPickPostStat] 사이를 구분하는 세로 구분선. */
@Composable
fun AniPickPostStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(10.dp)
            .background(AniPickTheme.colors.textGray),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickPostStatPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AniPickPostStat(icon = VisibilityOn, count = 102)
        AniPickPostStatDivider()
        AniPickPostStat(icon = HeartOutlined, count = 32)
        AniPickPostStatDivider()
        AniPickPostStat(icon = Comment, count = 8)
    }
}
