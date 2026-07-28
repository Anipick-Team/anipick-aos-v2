package com.jparkbro.auth.impl.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.ChevronLeft
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 뒤로가기 아이콘 + 타이틀 + 서브타이틀로 구성된, auth 하위 화면들(이메일 로그인/회원가입 등)이 공통으로 쓰는 헤더. */
@Composable
internal fun AuthScreenHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
    ) {
        Icon(
            imageVector = ChevronLeft,
            contentDescription = "뒤로가기 아이콘",
            tint = AniPickTheme.colors.black,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBackClick)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = AniPickTheme.typography.h1,
            color = AniPickTheme.colors.black,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.dimmed,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenHeaderPreview() {
    AuthScreenHeader(
        title = "이메일 로그인",
        subtitle = "회원 서비스 이용을 위해 로그인 해주세요.",
        onBackClick = {},
    )
}
