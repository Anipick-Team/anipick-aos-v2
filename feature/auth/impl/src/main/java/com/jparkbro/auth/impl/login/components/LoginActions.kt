package com.jparkbro.auth.impl.login.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.login.LoginAction
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun LoginActions(
    activity: Activity,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        SocialLoginButton(
            imageVector = ImageVector.vectorResource(R.drawable.kakao_login),
            contentDescription = "kakao login",
            onClick = { onAction(LoginAction.OnKakaoLoginClick(activity)) }
        )
        SocialLoginButton(
            imageVector = ImageVector.vectorResource(R.drawable.google_login),
            contentDescription = "google login",
            onClick = { onAction(LoginAction.OnGoogleLoginClick(activity)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이메일 로그인",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier
                    .clickable(
                        onClick = { onAction(LoginAction.OnEmailLoginClick) }
                    )
            )
            VerticalDivider(
                modifier = Modifier
                    .height(20.dp),
                thickness = 1.dp,
                color = AniPickTheme.colors.textGray
            )
            Text(
                text = "이메일 회원가입",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
                modifier = Modifier
                    .clickable(
                        onClick = { onAction(LoginAction.OnEmailSignupClick) }
                    )
            )
        }
    }
}

@Composable
private fun SocialLoginButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Image(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    )
}
