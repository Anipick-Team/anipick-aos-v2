package com.jparkbro.catalog.impl.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 검은 배너 박스 + 마스코트 이미지 헤더 - 문구만 다르게 얹어 재사용 */
@Composable
internal fun CatalogBannerHeader(
    modifier: Modifier = Modifier,
    title: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.black, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = title,
        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.home_banner_mascot),
            contentDescription = "배너 마스코트 이미지",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 76.dp, end = 24.dp),
        )
    }
}
