package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.ChevronLeft
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.util.orNullIfDefaultCover

/** 배너 이미지 위에 뒤로가기 아이콘과 커버 이미지가 겹치는 히어로 영역 */
@Composable
internal fun AnimeHeroBanner(
    title: String,
    bannerImageUrl: String?,
    coverImageUrl: String?,
    onBackClick: () -> Unit,
    onBannerClick: () -> Unit,
    onCoverClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimmedColor = AniPickTheme.colors.dimmed

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BANNER_HEIGHT),
    ) {
        AsyncImage(
            model = bannerImageUrl,
            contentDescription = "$title 배너 이미지",
            error = painterResource(R.drawable.banner_default_img),
            placeholder = painterResource(R.drawable.banner_default_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onBannerClick)
                .drawWithContent {
                    drawContent()
                    drawRect(color = dimmedColor)
                },
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 24.dp),
        ) {
            Icon(
                imageVector = ChevronLeft,
                contentDescription = "뒤로가기",
                tint = AniPickTheme.colors.white,
            )
        }
        AsyncImage(
            model = coverImageUrl.orNullIfDefaultCover(),
            contentDescription = "$title 커버 이미지",
            error = painterResource(R.drawable.portrait_default_img),
            placeholder = painterResource(R.drawable.portrait_default_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 36.dp)
                .width(132.dp)
                .aspectRatio(132f / 152f)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onCoverClick),
        )
    }
}

private val BANNER_HEIGHT = 220.dp

/** [AnimeHeroBanner]와 같은 배너/커버 배치로 로딩 중 자리를 채운다. 뒤로가기 아이콘은 로딩 중에도
 *  눌러야 하니 그대로 둔다. */
@Composable
internal fun AnimeHeroBannerSkeleton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BANNER_HEIGHT),
    ) {
        AniPickShimmerBox(modifier = Modifier.fillMaxSize())
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 24.dp),
        ) {
            Icon(
                imageVector = ChevronLeft,
                contentDescription = "뒤로가기",
                tint = AniPickTheme.colors.white,
            )
        }
        AniPickShimmerBox(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 36.dp)
                .width(132.dp)
                .aspectRatio(132f / 152f),
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AnimeHeroBannerPreview() {
    AnimeHeroBanner(
        title = "샘플 애니메이션",
        bannerImageUrl = "",
        coverImageUrl = "",
        onBackClick = {},
        onBannerClick = {},
        onCoverClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun AnimeHeroBannerSkeletonPreview() {
    AnimeHeroBannerSkeleton(onBackClick = {})
}
