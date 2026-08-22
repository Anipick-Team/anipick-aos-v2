package com.jparkbro.explore.impl.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.Genre

/** 커뮤니티 탭 목록 한 줄 - 커버 이미지 + 제목 + 장르 목록. 클릭하면 해당 커뮤니티로 이동한다. */
@Composable
internal fun ExploreCommunityBoardItem(
    board: CommunityBoard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = board.coverImageUrl,
            contentDescription = "${board.title} 커뮤니티 커버 이미지",
            error = painterResource(R.drawable.default_image_preference),
            placeholder = painterResource(R.drawable.default_image_preference),
            modifier = Modifier.size(width = 132.dp, height = 88.dp),
            contentScale = ContentScale.Crop,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = board.title ?: "-",
                color = AniPickTheme.colors.black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = AniPickTheme.typography.body2,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxLines = 1,
            ) {
                board.genres?.forEach { genre ->
                    AniPickGenreTag(genre = genre.name ?: "-")
                }
            }
        }
    }
}

@Composable
internal fun ExploreCommunityBoardItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, AniPickTheme.colors.backgroundGray, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AniPickShimmerBox(modifier = Modifier.size(width = 132.dp, height = 88.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AniPickShimmerBox(modifier = Modifier.width(120.dp).height(16.dp))
            AniPickShimmerBox(modifier = Modifier.width(80.dp).height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreCommunityBoardItemPreview() {
    ExploreCommunityBoardItem(
        board = CommunityBoard(
            hasBoard = true,
            seriesId = 1L,
            title = "프리렌: 장송의 여행",
            coverImageUrl = "",
            genres = listOf(Genre(id = 1, name = "판타지"), Genre(id = 2, name = "모험")),
            postCount = 128,
        ),
        onClick = {},
    )
}
