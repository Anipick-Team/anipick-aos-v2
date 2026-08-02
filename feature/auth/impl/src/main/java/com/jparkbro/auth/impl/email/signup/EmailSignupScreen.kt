package com.jparkbro.auth.impl.email.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.icon.Check
import com.jparkbro.core.designsystem.icon.ChevronRight
import com.jparkbro.core.designsystem.icon.VisibilityOff
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun EmailSignupRoot(
    onNavigateToPreferenceSetup: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EmailSignupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EmailSignupEvent.NavigateToPreferenceSetup -> onNavigateToPreferenceSetup()
        }
    }

    EmailSignupScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailSignupAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EmailSignupScreen(
    state: EmailSignupState,
    onAction: (EmailSignupAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp, 20.dp, 20.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                AuthScreenHeader(
                    title = "이메일 회원가입",
                    subtitle = "회원 서비스 이용을 위해 회원가입 해주세요.",
                    onBackClick = { onAction(EmailSignupAction.OnBackClick) },
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "이메일",
                        textField = {
                            AniPickBaseTextField(
                                state = state.emailState,
                                type = TextFieldType.TEXT,
                                placeholder = "이메일 입력",
                                maxLength = 50,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next,
                                ),
                                onKeyboardAction = { passwordFocusRequester.requestFocus() },
                            )
                        },
                    )
                    state.emailError?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "비밀번호",
                        labelTrailingContent = {
                            Icon(
                                imageVector = Check,
                                contentDescription = "체크",
                                tint = if (state.isPasswordValid) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        textField = {
                            AniPickBaseTextField(
                                state = state.passwordState,
                                modifier = Modifier.focusRequester(passwordFocusRequester),
                                type = TextFieldType.PASSWORD,
                                placeholder = "8~16자의 영문, 숫자, 특수문자",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                onKeyboardAction = {
                                    focusManager.clearFocus()
                                    if (state.isSignupEnabled) {
                                        onAction(EmailSignupAction.OnSignUpClick)
                                    }
                                },
                                actions = {
                                    Icon(
                                        imageVector = if (state.showPassword) VisibilityOn else VisibilityOff,
                                        contentDescription = "비밀번호 표시 전환",
                                        tint = AniPickTheme.colors.textGray,
                                        modifier = Modifier
                                            .clickable(
                                                onClick = { onAction(EmailSignupAction.OnPasswordVisibilityToggle) }
                                            )
                                    )
                                },
                                showPassword = state.showPassword,
                                maxLength = 50,
                            )
                        },
                    )
                    state.passwordError?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "애니픽 이용을 위해 동의가 필요해요.",
                        style = AniPickTheme.typography.h3,
                        color = AniPickTheme.colors.black
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AniPickTheme.colors.lightGray)
                            .clickable {
                                onAction(EmailSignupAction.OnAgreeAllToggle)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Check,
                            contentDescription = "체크",
                            tint = if (state.isAgreeAll) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Text(
                            text = "모두 동의합니다.",
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.black
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AgreementCheckRow(
                            label = "[필수] 만 14세 이상입니다.",
                            checked = state.isAgeChecked,
                            onToggle = { onAction(EmailSignupAction.OnAgeToggle) },
                        )
                        AgreementCheckRow(
                            label = "[필수] 이용약관에 동의합니다.",
                            checked = state.isTermsOfServiceChecked,
                            onToggle = { onAction(EmailSignupAction.OnTermsOfServiceToggle) },
                            onDetailClick = { onAction(EmailSignupAction.OnTermsOfServiceDetailClick) },
                        )
                        AgreementCheckRow(
                            label = "[필수] 개인정보 처리방침에 동의합니다.",
                            checked = state.isPrivacyPolicyChecked,
                            onToggle = { onAction(EmailSignupAction.OnPrivacyPolicyToggle) },
                            onDetailClick = { onAction(EmailSignupAction.OnPrivacyPolicyDetailClick) },
                        )
                    }
                    state.termsError?.let { message ->
                        Text(
                            text = message,
                            style = AniPickTheme.typography.caption1,
                            color = AniPickTheme.colors.point,
                        )
                    }
                }
                AniPickButton(
                    text = "가입하기",
                    onClick = { onAction(EmailSignupAction.OnSignUpClick) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.isSignupEnabled,
                    isLoading = state.isLoading,
                )
            }
        }
    }
}

/**
 * 약관 동의 항목 한 줄(체크 아이콘 + 라벨, 선택적으로 자세히 보기 화살표).
 * "만 14세 이상"/"이용약관"/"개인정보 처리방침" 세 항목이 구조가 동일해서 하나로 뺐다.
 *
 * @param onDetailClick 약관 전문을 보여줄 상세 화면/다이얼로그로 이동하는 콜백. null이면 화살표 자체를 그리지 않는다("14세 이상" 항목처럼 상세 볼 내용이 없는 경우).
 */
@Composable
private fun AgreementCheckRow(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onToggle),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Check,
                contentDescription = "체크",
                tint = if (checked) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = label,
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.black,
            )
        }
        if (onDetailClick != null) {
            Icon(
                imageVector = ChevronRight,
                contentDescription = "자세히 보기",
                tint = AniPickTheme.colors.gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onDetailClick),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmailSignupScreenPreview() {
    EmailSignupScreen(
        state = EmailSignupState(),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun EmailSignupScreenAgreedPreview() {
    EmailSignupScreen(
        state = EmailSignupState(
            isAgeChecked = true,
            isTermsOfServiceChecked = true,
            isPrivacyPolicyChecked = true,
            isSignupEnabled = true,
        ),
        onAction = {}
    )
}
