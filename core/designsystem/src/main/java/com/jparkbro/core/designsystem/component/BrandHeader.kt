package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 화면 전환 시 [AniPickBrandHeader]를 shared element로 이어붙일 때 쓰는 공용 key */
const val AniPickBrandHeaderKey = "ani_pick_brand_header"

@Composable
fun AniPickBrandHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.app_logo),
            contentDescription = "app logo",
            modifier = Modifier
                .size(210.dp, 44.dp)
        )
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = "나에게 딱 맞는 애니 추천을 위해.",
            style = AniPickTheme.typography.h2,
            color = AniPickTheme.colors.black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "사용할수록 더 좋아지는 애니메이션 환경을 만나보세요",
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.dimmed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickBrandHeaderPreview() {
    AniPickBrandHeader()
}
