package com.jparkbro.mypage.impl.setting.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.user.snsLabel
import com.jparkbro.core.ui.ObserveAsEvents
import com.jparkbro.mypage.api.SettingDetailType
import com.jparkbro.mypage.impl.BuildConfig
import com.jparkbro.mypage.impl.setting.main.components.SettingItemRow
import com.jparkbro.mypage.impl.setting.main.components.SettingSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SettingMainRoot(
    onBackClick: () -> Unit,
    onNavigateToDetail: (SettingDetailType) -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingMainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingMainEvent.LogoutConfirmed -> onLogout()
        }
    }

    SettingMainScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SettingMainAction.OnBackClick -> onBackClick()
                is SettingMainAction.OnEditProfileClick -> onNavigateToDetail(action.type)
                SettingMainAction.OnContactClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                    context.startActivity(intent)
                }
                SettingMainAction.OnTermsClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/terms.html".toUri())
                    context.startActivity(intent)
                }
                SettingMainAction.OnPrivacyPolicyClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/privacy.html".toUri())
                    context.startActivity(intent)
                }
                SettingMainAction.OnOpenSourceLicenseClick -> {
                    OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                }
                SettingMainAction.OnNoticeClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://spiral-cowl-f89.notion.site/227b3eed42088098a351ff047659bdcb?source=copy_link".toUri())
                    context.startActivity(intent)
                }
                SettingMainAction.OnLogoutClick,
                SettingMainAction.OnLogoutConfirm,
                SettingMainAction.OnLogoutDialogDismiss,
                -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun SettingMainScreen(
    state: SettingMainState,
    onAction: (SettingMainAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "설정",
                onBackClick = { onAction(SettingMainAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            item {
                Column(
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
                            onClick = {  },
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
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    SettingSection(
                        title = "앱 설정",
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        SettingItemRow(
                            label = "앱 버전",
                            onClick = {  },
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
            item {
                SettingSection(
                    title = "기타",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    SettingItemRow(
                        label = "로그아웃",
                        onClick = { onAction(SettingMainAction.OnLogoutClick) },
                        labelColor = AniPickTheme.colors.point,
                    )
                    SettingItemRow(
                        label = "회원탈퇴",
                        onClick = { onAction(SettingMainAction.OnEditProfileClick(SettingDetailType.Withdrawal)) },
                        labelColor = AniPickTheme.colors.point,
                    )
                }
            }
        }
    }

    if (state.showLogoutDialog) {
        AniPickDialog(
            title = "로그아웃",
            message = "로그아웃 하시겠습니까?",
            onDismissRequest = { onAction(SettingMainAction.OnLogoutDialogDismiss) },
            confirmText = "로그아웃",
            onConfirm = { onAction(SettingMainAction.OnLogoutConfirm) },
            dismissText = "취소",
            onDismiss = { onAction(SettingMainAction.OnLogoutDialogDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingMainScreenPreview() {
    SettingMainScreen(
        state = SettingMainState(),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun SettingMainScreenLoadingPreview() {
    SettingMainScreen(
        state = SettingMainState(isLoading = true),
        onAction = {},
    )
}
