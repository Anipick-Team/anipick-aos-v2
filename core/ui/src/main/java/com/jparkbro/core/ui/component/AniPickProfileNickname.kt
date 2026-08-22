package com.jparkbro.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 리뷰/댓글/게시글 등에서 공통으로 쓰는 프로필 이미지 + 닉네임 - 원형 이미지(기본 실패/로딩 시 기본 프로필) + 닉네임 텍스트. */
@Composable
fun AniPickProfileNickname(
    profileImageUrl: String?,
    nickname: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = 30.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = null,
            error = painterResource(R.drawable.profile_default_img),
            placeholder = painterResource(R.drawable.profile_default_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
        )
        nickname?.let {
            Text(
                text = it,
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickProfileNicknamePreview() {
    AniPickProfileNickname(profileImageUrl = "", nickname = "anipick_x2k3")
}
