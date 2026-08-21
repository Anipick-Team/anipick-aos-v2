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
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme

private val FALLBACK_IMAGES = listOf(
    R.drawable.fallback_image_1,
    R.drawable.fallback_image_2,
    R.drawable.fallback_image_3,
)

/** 보여줄 콘텐츠가 없을 때 넣는 공통 컴포넌트 - 랜덤 이미지는 처음 구성될 때 한 번만 고정된다.
 *  [onRetryClick]이 있으면(=조회 자체가 실패한 경우) 아래에 재시도 버튼을 붙인다 - 단순히 결과가
 *  빈 경우(=정상 응답인데 데이터가 없음)에는 재시도해도 결과가 똑같으니 null로 둬서 버튼을 뺀다. */
@Composable
fun AniPickEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    retryText: String = "다시 시도",
    onRetryClick: (() -> Unit)? = null,
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
        if (onRetryClick != null) {
            AniPickButton(
                text = retryText,
                onClick = onRetryClick,
                size = ButtonSize.S,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickEmptyStatePreview() {
    AniPickEmptyState(message = "해당하는 작품이 없습니다.")
}

@Preview(showBackground = true)
@Composable
private fun AniPickEmptyStateWithRetryPreview() {
    AniPickEmptyState(
        message = "네트워크 연결을 확인해주세요.",
        onRetryClick = {},
    )
}
