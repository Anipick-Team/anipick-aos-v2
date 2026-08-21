package com.jparkbro.mypage.impl.setting.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.effect.ObserveAsEvents
import com.jparkbro.mypage.api.SettingDetailType
import com.jparkbro.mypage.impl.setting.detail.components.EmailFields
import com.jparkbro.mypage.impl.setting.detail.components.NicknameFields
import com.jparkbro.mypage.impl.setting.detail.components.PasswordFields
import com.jparkbro.mypage.impl.setting.detail.components.WithdrawalFields
import com.jparkbro.mypage.impl.setting.detail.components.title
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun SettingDetailRoot(
    type: SettingDetailType,
    onBackClick: () -> Unit,
    onWithdrawSuccess: () -> Unit,
    viewModel: SettingDetailViewModel = koinViewModel(parameters = { parametersOf(type) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingDetailEvent.SaveSuccess -> onBackClick()
            SettingDetailEvent.WithdrawSuccess -> onWithdrawSuccess()
        }
    }

    SettingDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SettingDetailAction.OnBackClick -> onBackClick()
                SettingDetailAction.OnSaveClick,
                SettingDetailAction.OnPasswordVisibilityToggle,
                SettingDetailAction.OnWithdrawConfirm,
                SettingDetailAction.OnWithdrawDialogDismiss,
                -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun SettingDetailScreen(
    state: SettingDetailState,
    onAction: (SettingDetailAction) -> Unit,
) {
    val emailPasswordFocusRequester = remember { FocusRequester() }
    val newPasswordFocusRequester = remember { FocusRequester() }
    val checkNewPasswordFocusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = state.type.title(),
                onBackClick = { onAction(SettingDetailAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp, top = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp),
            ) {
                when (state.type) {
                    SettingDetailType.Nickname -> NicknameFields(state = state)
                    SettingDetailType.Email -> EmailFields(
                        state = state,
                        onAction = onAction,
                        emailPasswordFocusRequester = emailPasswordFocusRequester,
                    )
                    SettingDetailType.Password -> PasswordFields(
                        state = state,
                        onAction = onAction,
                        newPasswordFocusRequester = newPasswordFocusRequester,
                        checkNewPasswordFocusRequester = checkNewPasswordFocusRequester,
                    )
                    SettingDetailType.Withdrawal -> WithdrawalFields(state = state)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (state.type == SettingDetailType.Withdrawal) {
                    Text(
                        text = "정말로 탈퇴하시겠습니까?",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.primary,
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = AniPickTheme.colors.backgroundGray
                )
                AniPickButton(
                    text = if (state.type == SettingDetailType.Withdrawal) "탈퇴하기" else "저장",
                    onClick = { onAction(SettingDetailAction.OnSaveClick) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = when (state.type) {
                        SettingDetailType.Nickname -> state.isChangeNicknameEnabled
                        SettingDetailType.Email -> state.isChangeEmailEnabled
                        SettingDetailType.Password -> state.isChangePasswordEnabled
                        SettingDetailType.Withdrawal -> !state.isLoading
                    },
                    isLoading = state.isLoading,
                    backgroundColor = AniPickTheme.colors.primary,
                )
            }
        }
    }

    if (state.showWithdrawDialog) {
        AniPickDialog(
            title = "회원탈퇴",
            message = "회원탈퇴 하시겠습니까?",
            onDismissRequest = { onAction(SettingDetailAction.OnWithdrawDialogDismiss) },
            confirmText = "탈퇴하기",
            onConfirm = { onAction(SettingDetailAction.OnWithdrawConfirm) },
            dismissText = "취소",
            onDismiss = { onAction(SettingDetailAction.OnWithdrawDialogDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingDetailScreenNicknamePreview() {
    SettingDetailScreen(
        state = SettingDetailState(type = SettingDetailType.Nickname, currentNickname = "기존닉네임"),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun SettingDetailScreenEmailPreview() {
    SettingDetailScreen(
        state = SettingDetailState(type = SettingDetailType.Email, currentEmail = "email@email.com"),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun SettingDetailScreenPasswordPreview() {
    SettingDetailScreen(
        state = SettingDetailState(type = SettingDetailType.Password),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun SettingDetailScreenWithdrawalPreview() {
    SettingDetailScreen(
        state = SettingDetailState(type = SettingDetailType.Withdrawal),
        onAction = {},
    )
}
