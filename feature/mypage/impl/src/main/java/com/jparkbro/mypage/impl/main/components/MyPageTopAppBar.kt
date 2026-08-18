package com.jparkbro.mypage.impl.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.net.Uri
import coil3.compose.AsyncImage
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.icon.Edit
import com.jparkbro.core.designsystem.icon.Setting
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.rememberPhotoPickerWithPermission

@Composable
internal fun MyPageTopAppBar(
    profileImage: String?,
    onSettingClick: () -> Unit,
    onProfileImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(TOP_APP_BAR_HEIGHT)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "마이페이지",
                style = AniPickTheme.typography.h1,
                color = AniPickTheme.colors.black,
            )
            Box(
                modifier = Modifier
                    .border(2.dp, AniPickTheme.colors.textGray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onSettingClick)
                    .padding(8.dp),
            ) {
                Icon(
                    imageVector = Setting,
                    contentDescription = "설정 페이지 이동 아이콘",
                    modifier = Modifier.size(18.dp),
                    tint = AniPickTheme.colors.textGray,
                )
            }
        }
        ProfileImage(
            profileImage = profileImage,
            onProfileImageSelected = onProfileImageSelected,
        )
    }
}

@Composable
private fun ProfileImage(
    profileImage: String?,
    onProfileImageSelected: (Uri) -> Unit,
) {
    val launchPhotoPicker = rememberPhotoPickerWithPermission(onImageSelected = onProfileImageSelected)

    Box(
        modifier = Modifier.clickable(onClick = launchPhotoPicker),
    ) {
        AsyncImage(
            model = profileImage,
            contentDescription = "프로필 이미지",
            error = painterResource(R.drawable.profile_default_img),
            placeholder = painterResource(R.drawable.profile_default_img),
            modifier = Modifier
                .padding(end = 16.dp)
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Icon(
            imageVector = Edit,
            contentDescription = "수정 아이콘",
            tint = AniPickTheme.colors.white,
            modifier = Modifier
                .background(AniPickTheme.colors.primary, CircleShape)
                .padding(6.dp)
                .size(20.dp)
                .align(Alignment.TopEnd),
        )
    }
}

private val TOP_APP_BAR_HEIGHT = 60.dp

@Composable
@Preview(showBackground = true)
private fun MyPageTopAppBarPreview() {
    MyPageTopAppBar(
        profileImage = null,
        onSettingClick = {},
        onProfileImageSelected = {},
    )
}
