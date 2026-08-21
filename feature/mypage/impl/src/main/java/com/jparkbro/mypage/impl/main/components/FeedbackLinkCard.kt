package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.util.DevicePreviews

private val FeedbackGradientColors = listOf(Color(0xFF186D97), Color(0xFF16A1A8), Color(0xFF5CC398))

@Composable
internal fun FeedbackLinkCard(
    modifier: Modifier = Modifier,
    onFeedbackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .widthIn(max = 400.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = FeedbackGradientColors,
                    start = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                    end = Offset(0f, 0f),
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(start = 24.dp, top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Text(
                    text = "애니픽을 사용해 보셨나요?",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.white
                )
                Text(
                    text = "더 좋은 서비스를 만들 수 있도록 여러분의 의견을 들려주세요.",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.white
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AniPickTheme.colors.white)
                    .clickable(onClick = onFeedbackClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "바로가기",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.black,

                )
            }
        }
        Image(
            painter = painterResource(R.drawable.feedback_card_cha),
            contentDescription = "피드백 마스코트 이미지",
            modifier = Modifier
                .wrapContentHeight(Alignment.Bottom)
                .padding(start = 24.dp, end = 16.dp)
                .width(100.dp)
        )
    }
}

@DevicePreviews
@Composable
private fun FeedbackLinkCardPreview() {
    FeedbackLinkCard(
        onFeedbackClick = {}
    )
}
