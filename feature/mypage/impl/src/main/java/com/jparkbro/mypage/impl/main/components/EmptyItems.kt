package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun EmptyItems(
    contentText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.lightGray, RoundedCornerShape(8.dp))
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.mypage_empty_items),
            contentDescription = "Empty Items 안내 이미지",
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
        Text(
            text = contentText,
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
        )
    }
}

@Preview
@Composable
private fun EmptyItemsPreview() {
    EmptyItems(
        "아직 좋아요한 작품이 없어요.\n좋아요를 누르러 가볼까요 ?"
    )
}