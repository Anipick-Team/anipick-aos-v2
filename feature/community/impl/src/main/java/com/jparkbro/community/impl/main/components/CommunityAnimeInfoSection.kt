package com.jparkbro.community.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jparkbro.community.impl.main.CommunityMainAction
import com.jparkbro.community.impl.main.CommunityPostFilter
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickGenreTag
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 커뮤니티 상단 애니 정보 - 커버 이미지 + 제목 + 장르 + 게시글 필터. 셋 다 진입 시 넘겨받은
 *  [com.jparkbro.core.model.community.CommunityBoard] 값 그대로라 별도 조회가 없다.
 *  [title]이 null이면(=이 화면 진입 경로가 애니 정보를 안 들고 있는 경우) 애니 정보 부분은 생략하고 필터만 보여준다. */
@Composable
internal fun CommunityAnimeInfoSection(
    title: String?,
    coverImageUrl: String?,
    genres: List<String>,
    postFilter: CommunityPostFilter,
    isSpoilerVisible: Boolean,
    onAction: (CommunityMainAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (title != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = coverImageUrl.orNullIfDefaultCover(),
                    contentDescription = "$title 커버 이미지",
                    error = painterResource(R.drawable.portrait_default_img),
                    placeholder = painterResource(R.drawable.portrait_default_img),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(132.dp)
                        .aspectRatio(132f / 152f)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = title,
                        style = AniPickTheme.typography.body1,
                        color = AniPickTheme.colors.black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (genres.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            genres.forEach { genre -> AniPickGenreTag(genre = genre) }
                        }
                    }
                }
            }
        }

        CommunityFilterSection(
            selectedFilter = postFilter,
            isSpoilerVisible = isSpoilerVisible,
            onFilterClick = { filter -> onAction(CommunityMainAction.OnFilterClick(filter)) },
            onSpoilerVisibleChange = { isVisible -> onAction(CommunityMainAction.OnSpoilerVisibleChange(isVisible)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityAnimeInfoSectionPreview() {
    CommunityAnimeInfoSection(
        title = "프리렌: 장송의 여행",
        coverImageUrl = "",
        genres = listOf("판타지", "모험"),
        postFilter = CommunityPostFilter.ALL,
        isSpoilerVisible = true,
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CommunityAnimeInfoSectionNoTitlePreview() {
    CommunityAnimeInfoSection(
        title = null,
        coverImageUrl = null,
        genres = emptyList(),
        postFilter = CommunityPostFilter.ALL,
        isSpoilerVisible = true,
        onAction = {},
    )
}
