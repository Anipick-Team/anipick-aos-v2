package com.jparkbro.mypage.impl.setting.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.mypage.impl.BuildConfig
import com.jparkbro.mypage.impl.setting.main.SettingMainAction

/** "앱 설정" 섹션 - SettingMainScreen LazyColumn의 item. */
@Composable
internal fun SettingAppSection(
    onAction: (SettingMainAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        SettingSection(
            title = "앱 설정",
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            SettingItemRow(
                label = "앱 버전",
                onClick = { },
                trailingContent = {
                    Text(
                        text = BuildConfig.APP_VERSION,
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.textGray
                    )
                },
            )
            SettingItemRow(
                label = "문의하기",
                onClick = { onAction(SettingMainAction.OnContactClick) },
                trailingContent = {
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "화면이동 아이콘",
                        modifier = Modifier.size(18.dp),
                        tint = AniPickTheme.colors.textGray
                    )
                },
            )
            SettingItemRow(
                label = "서비스 이용약관",
                onClick = { onAction(SettingMainAction.OnTermsClick) },
                trailingContent = {
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "화면이동 아이콘",
                        modifier = Modifier.size(18.dp),
                        tint = AniPickTheme.colors.textGray
                    )
                },
            )
            SettingItemRow(
                label = "개인정보 처리방침",
                onClick = { onAction(SettingMainAction.OnPrivacyPolicyClick) },
                trailingContent = {
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "화면이동 아이콘",
                        modifier = Modifier
                            .size(18.dp),
                        tint = AniPickTheme.colors.textGray
                    )
                },
            )
            SettingItemRow(
                label = "오픈소스 라이선스",
                onClick = { onAction(SettingMainAction.OnOpenSourceLicenseClick) },
                trailingContent = {
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "화면이동 아이콘",
                        modifier = Modifier.size(18.dp),
                        tint = AniPickTheme.colors.textGray
                    )
                },
            )
            SettingItemRow(
                label = "공지사항",
                onClick = { onAction(SettingMainAction.OnNoticeClick) },
                trailingContent = {
                    Icon(
                        imageVector = ChevronRight,
                        contentDescription = "화면이동 아이콘",
                        modifier = Modifier
                            .size(18.dp),
                        tint = AniPickTheme.colors.textGray
                    )
                },
            )
        }
        AniPickSectionDivider()
    }
}
