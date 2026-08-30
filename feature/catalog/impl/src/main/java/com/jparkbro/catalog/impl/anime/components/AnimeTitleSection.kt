package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickAnimatedHeartIcon
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.Share
import com.jparkbro.core.designsystem.icon.StarFilled
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 배너 아래 제목/찜/공유, 별점을 묶은 섹션. */
@Composable
internal fun AnimeTitleSection(
    title: String,
    averageRating: String,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = AniPickTheme.typography.h2,
                    color = AniPickTheme.colors.black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(weight = 1f, fill = false),
                )
                AniPickAnimatedHeartIcon(
                    isLiked = isLiked,
                    onClick = onLikeClick,
                    contentDescription = "찜하기",
                    unlikedTint = AniPickTheme.colors.textGray,
                    size = 20.dp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, AniPickTheme.colors.textGray, RoundedCornerShape(8.dp))
                    .clickable(onClick = onShareClick)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Share,
                    contentDescription = "공유하기",
                    tint = AniPickTheme.colors.textGray,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = StarFilled,
                contentDescription = null,
                tint = AniPickTheme.colors.point,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = averageRating.ifBlank { "0.0" },
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.point,
            )
        }
    }
}

/** [AnimeTitleSection]과 같은 배치에서, 서버 데이터로 채워지는 제목/평점만 셰이머로 대체한다.
 *  찜/공유는 데이터와 무관한 고정 UI라 그대로 둔다. */
@Composable
internal fun AnimeTitleSectionSkeleton(
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AniPickShimmerBox(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false)
                        .fillMaxWidth(0.6f)
                        .height(28.dp),
                )
                AniPickAnimatedHeartIcon(
                    isLiked = false,
                    onClick = {},
                    contentDescription = "찜하기",
                    unlikedTint = AniPickTheme.colors.textGray,
                    size = 20.dp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, AniPickTheme.colors.textGray, RoundedCornerShape(8.dp))
                    .clickable(onClick = onShareClick)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Share,
                    contentDescription = "공유하기",
                    tint = AniPickTheme.colors.textGray,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = StarFilled,
                contentDescription = null,
                tint = AniPickTheme.colors.point,
                modifier = Modifier.size(24.dp),
            )
            AniPickShimmerBox(modifier = Modifier.width(28.dp).height(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AnimeTitleSectionPreview() {
    AnimeTitleSection(
        title = "장송의 프리렌 2기",
        averageRating = "2.3",
        isLiked = false,
        onLikeClick = {},
        onShareClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun AnimeTitleSectionSkeletonPreview() {
    AnimeTitleSectionSkeleton(onShareClick = {})
}
