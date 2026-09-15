package com.jparkbro.community.impl.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 게시글 첨부 이미지 슬라이드 - 페이지 인디케이터 포함. 각 원소는 [ByteArray](인증 조회 완료) 또는 [String](URL, 폴백). */
@Composable
internal fun CommunityDetailImagePager(
    images: List<Any>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = modifier) {
        HorizontalPager(state = pagerState) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                error = painterResource(R.drawable.banner_default_img),
                placeholder = painterResource(R.drawable.banner_default_img),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            )
        }
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(images.size) { page ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(
                                if (page == pagerState.currentPage) {
                                    AniPickTheme.colors.black
                                } else {
                                    AniPickTheme.colors.gray
                                }
                            ),
                    )
                }
            }
        }
    }
}
