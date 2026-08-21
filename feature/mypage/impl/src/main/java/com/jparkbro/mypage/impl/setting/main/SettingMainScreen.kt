package com.jparkbro.mypage.impl.setting.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.effect.ObserveAsEvents
import com.jparkbro.mypage.api.SettingDetailType
import com.jparkbro.mypage.impl.setting.main.components.SettingAccountSection
import com.jparkbro.mypage.impl.setting.main.components.SettingAppSection
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
                SettingAccountSection(state = state, onAction = onAction)
            }
            item {
                SettingAppSection(onAction = onAction)
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
