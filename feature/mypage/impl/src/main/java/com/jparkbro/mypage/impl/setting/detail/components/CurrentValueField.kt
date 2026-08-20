package com.jparkbro.mypage.impl.setting.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** "기존 닉네임"/"기존 이메일"처럼 수정 불가능한 현재 값을 라벨 + 회색 박스로 보여준다. */
@Composable
internal fun CurrentValueField(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = AniPickTheme.typography.h3,
            color = AniPickTheme.colors.black,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .background(AniPickTheme.colors.lightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = value,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
            )
        }
    }
}
