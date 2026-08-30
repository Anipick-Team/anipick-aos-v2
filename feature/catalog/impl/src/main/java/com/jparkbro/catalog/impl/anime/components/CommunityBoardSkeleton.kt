package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 커뮤니티 탭 클릭 후 게시판 존재 여부(`getCommunityBoardByAnime`)를 조회하는 동안 보여주는 화면.
 *  응답에 따라 커뮤니티로 이동하거나 생성 안내 다이얼로그가 뜨고 그 자리엔 아무 콘텐츠도 안 남으므로,
 *  콘텐츠 모양을 예고하는 스켈레톤 대신 스피너+안내 문구를 쓴다. */
@Composable
internal fun CommunityBoardSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AniPickTheme.colors.white),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = AniPickTheme.colors.primary,
                strokeWidth = 3.dp,
            )
            Text(
                text = "커뮤니티 확인 중...",
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityBoardSkeletonPreview() {
    CommunityBoardSkeleton()
}
