package com.jparkbro.mypage.impl.setting.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.user.snsLabel
import com.jparkbro.mypage.api.SettingDetailType
import com.jparkbro.mypage.impl.setting.main.SettingMainAction
import com.jparkbro.mypage.impl.setting.main.SettingMainState

/** "계정" 섹션 - SettingMainScreen LazyColumn의 item. */
@Composable
internal fun SettingAccountSection(
    state: SettingMainState,
    onAction: (SettingMainAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        SettingSection(
            title = "계정",
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            SettingItemRow(
                label = "닉네임 변경",
                isEnabled = !state.isLoading,
                onClick = { onAction(SettingMainAction.OnEditProfileClick(SettingDetailType.Nickname)) },
                trailingContent = {
                    if (state.isLoading) {
                        AniPickShimmerBox(modifier = Modifier.width(72.dp).height(16.dp))
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.nickname.orEmpty(),
                                style = AniPickTheme.typography.body2,
                                color = AniPickTheme.colors.textGray
                            )
                            Icon(
                                imageVector = ChevronRight,
                                contentDescription = "화면이동 아이콘",
                                modifier = Modifier
                                    .size(18.dp),
                                tint = AniPickTheme.colors.textGray
                            )
                        }
                    }
                },
            )
            SettingItemRow(
                label = "이메일 변경",
                isEnabled = !state.isLoading && state.canEditCredentials,
                onClick = { onAction(SettingMainAction.OnEditProfileClick(SettingDetailType.Email)) },
                trailingContent = {
                    if (state.isLoading) {
                        AniPickShimmerBox(modifier = Modifier.width(120.dp).height(16.dp))
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.email.orEmpty(),
                                style = AniPickTheme.typography.body2,
                                color = if (state.canEditCredentials) AniPickTheme.colors.textGray else AniPickTheme.colors.gray
                            )
                            Icon(
                                imageVector = ChevronRight,
                                contentDescription = "화면이동 아이콘",
                                modifier = Modifier
                                    .size(18.dp),
                                tint = if (state.canEditCredentials) AniPickTheme.colors.textGray else AniPickTheme.colors.gray
                            )
                        }
                    }
                },
            )
            SettingItemRow(
                label = "비밀번호 변경",
                isEnabled = !state.isLoading && state.canEditCredentials,
                onClick = { onAction(SettingMainAction.OnEditProfileClick(SettingDetailType.Password)) },
                trailingContent = {
                    if (state.isLoading) {
                        AniPickShimmerBox(modifier = Modifier.width(90.dp).height(16.dp))
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (!state.canEditCredentials) {
                                Text(
                                    text = "sns 간편가입된 계정입니다.",
                                    style = AniPickTheme.typography.body2,
                                    color = AniPickTheme.colors.gray
                                )
                            } else {
                                Icon(
                                    imageVector = ChevronRight,
                                    contentDescription = "화면이동 아이콘",
                                    modifier = Modifier.size(18.dp),
                                    tint = if (state.canEditCredentials) AniPickTheme.colors.textGray else AniPickTheme.colors.gray
                                )
                            }
                        }
                    }
                },
            )
            SettingItemRow(
                label = "연동 SNS",
                isEnabled = false,
                onClick = { },
                trailingContent = {
                    if (state.isLoading) {
                        AniPickShimmerBox(modifier = Modifier.width(50.dp).height(16.dp))
                    } else {
                        Text(
                            text = state.provider.snsLabel(),
                            style = AniPickTheme.typography.body2,
                            color = AniPickTheme.colors.primary
                        )
                    }
                },
            )
        }
        AniPickSectionDivider()
    }
}
