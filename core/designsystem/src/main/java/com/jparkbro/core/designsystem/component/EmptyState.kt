package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

private val FALLBACK_IMAGES = listOf(
    R.drawable.fallback_image_1,
    R.drawable.fallback_image_2,
    R.drawable.fallback_image_3,
)

/**
 * 검색 결과 없음, 네트워크 에러 등 "보여줄 콘텐츠가 없을 때" 전체 화면(LazyColumn 자리 전체 등)에
 * 대신 넣는 공통 컴포넌트. fallback_image_1~3 중 하나를 무작위로 보여주고 그 아래에 [message]를 띄운다.
 * 이미지는 이 컴포저블이 처음 구성될 때 한 번만 골라서(remember), 리컴포지션마다 바뀌지 않는다.
 */
@Composable
fun AniPickEmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    val imageRes = remember { FALLBACK_IMAGES.random() }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.size(148.dp),
        )
        Text(
            text = message,
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.textGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickEmptyStatePreview() {
    AniPickEmptyState(message = "해당하는 작품이 없습니다.\n인터넷 연결을 확인해주세요.")
}
