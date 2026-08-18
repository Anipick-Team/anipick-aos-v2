package com.jparkbro.mypage.impl.setting.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import com.jparkbro.mypage.api.SettingDetailType
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
    val focusManager = LocalFocusManager.current
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
                    SettingDetailType.Nickname -> {
                        CurrentValueField(label = "기존 닉네임", value = state.currentNickname.orEmpty())
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "새 닉네임",
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.nicknameState,
                                        type = TextFieldType.TEXT,
                                        placeholder = "새 닉네임 입력",
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        onKeyboardAction = { focusManager.clearFocus() },
                                    )
                                },
                            )
                            state.nicknameError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                        }
                    }
                    SettingDetailType.Email -> {
                        CurrentValueField(label = "기존 이메일", value = state.currentEmail.orEmpty())
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "새 이메일",
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.emailState,
                                        type = TextFieldType.TEXT,
                                        placeholder = "새 이메일 입력",
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next,
                                        ),
                                        onKeyboardAction = { emailPasswordFocusRequester.requestFocus() },
                                    )
                                },
                            )
                            state.emailError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "비밀번호",
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.emailPasswordState,
                                        modifier = Modifier.focusRequester(emailPasswordFocusRequester),
                                        type = TextFieldType.PASSWORD,
                                        placeholder = "본인 확인을 위해 비밀번호를 입력하세요",
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        onKeyboardAction = { focusManager.clearFocus() },
                                        actions = {
                                            AniPickPasswordVisibilityToggleIcon(
                                                showPassword = state.showPassword,
                                                onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                                            )
                                        },
                                        showPassword = state.showPassword,
                                    )
                                },
                            )
                            state.emailPasswordError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = "• 반드시 본인 명의의 이메일을 입력해주세요.",
                                    style = AniPickTheme.typography.caption2,
                                    color = AniPickTheme.colors.textGray,
                                )
                                Text(
                                    text = "• 본 이메일은 계정 분실 시 아이디 및 비밀번호 찾기, 개인정보 관련 주요 고지사항 안내 등에 사용됩니다.",
                                    style = AniPickTheme.typography.caption2,
                                    color = AniPickTheme.colors.textGray,
                                )
                            }
                        }
                    }
                    SettingDetailType.Password -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "현재 비밀번호",
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.currentPasswordState,
                                        type = TextFieldType.PASSWORD,
                                        placeholder = "현재 비밀번호를 입력하세요",
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        onKeyboardAction = { newPasswordFocusRequester.requestFocus() },
                                        actions = {
                                            AniPickPasswordVisibilityToggleIcon(
                                                showPassword = state.showPassword,
                                                onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                                            )
                                        },
                                        showPassword = state.showPassword,
                                    )
                                },
                            )
                            state.currentPasswordError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "새 비밀번호",
                                labelTrailingContent = {
                                    AniPickValidationCheckIcon(isValid = state.isNewPasswordValid)
                                },
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.newPasswordState,
                                        modifier = Modifier.focusRequester(newPasswordFocusRequester),
                                        type = TextFieldType.PASSWORD,
                                        placeholder = "8~16자의 영문, 숫자, 특수문자",
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        onKeyboardAction = { checkNewPasswordFocusRequester.requestFocus() },
                                        actions = {
                                            AniPickPasswordVisibilityToggleIcon(
                                                showPassword = state.showPassword,
                                                onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                                            )
                                        },
                                        showPassword = state.showPassword,
                                    )
                                },
                            )
                            state.newPasswordError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AniPickLabeledField(
                                label = "새 비밀번호 확인",
                                labelTrailingContent = {
                                    AniPickValidationCheckIcon(isValid = state.isPasswordMatch)
                                },
                                textField = {
                                    AniPickBaseTextField(
                                        state = state.checkNewPasswordState,
                                        modifier = Modifier.focusRequester(checkNewPasswordFocusRequester),
                                        type = TextFieldType.PASSWORD,
                                        placeholder = "새 비밀번호를 다시 입력하세요",
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        onKeyboardAction = { focusManager.clearFocus() },
                                        actions = {
                                            AniPickPasswordVisibilityToggleIcon(
                                                showPassword = state.showPassword,
                                                onToggle = { onAction(SettingDetailAction.OnPasswordVisibilityToggle) },
                                            )
                                        },
                                        showPassword = state.showPassword,
                                    )
                                },
                            )
                            state.newPasswordConfirmError?.let { errorMessage ->
                                AniPickErrorText(errorMessage)
                            }
                        }
                    }
                    SettingDetailType.Withdrawal -> {
                        CurrentValueField(label = "계정 이메일", value = state.currentEmail.orEmpty())
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "탈퇴 시 주의사항",
                                style = AniPickTheme.typography.h2,
                                color = AniPickTheme.colors.black,
                            )
                            Text(
                                text = "탈퇴 시, 회원정보는 당사의 개인정보처리방침에 따라 삭제 또는 격리하여 보존 조치되며, 삭제된 데이터는 복구가 불가능합니다. 서비스 내에서 남긴 리뷰는 탈퇴 후에 자동 삭제되지 않습니다.",
                                style = AniPickTheme.typography.caption1,
                                color = AniPickTheme.colors.textGray,
                            )
                            AniPickErrorText(
                                "'회원탈퇴'를 누르는 것은 상기 안내사항을 모두 확인하였으며 이에 동의함을 의미합니다."
                            )
                        }
                    }
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

/** "기존 닉네임"/"기존 이메일"처럼 수정 불가능한 현재 값을 라벨 + 회색 박스로 보여준다. */
@Composable
private fun CurrentValueField(label: String, value: String) {
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

private fun SettingDetailType.title(): String = when (this) {
    SettingDetailType.Nickname -> "닉네임 변경"
    SettingDetailType.Email -> "이메일 변경"
    SettingDetailType.Password -> "비밀번호 변경"
    SettingDetailType.Withdrawal -> "회원 탈퇴"
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
