package com.jparkbro.auth.impl.email.signup

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.auth.impl.component.AuthScaffold
import com.jparkbro.auth.impl.component.AuthScreenHeader
import com.jparkbro.core.designsystem.component.AniPickPasswordVisibilityToggleIcon
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.icon.ChevronRight
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
    val context = LocalContext.current
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
                EmailSignupAction.OnTermsOfServiceDetailClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/terms.html".toUri())
                    context.startActivity(intent)
                }
                EmailSignupAction.OnPrivacyPolicyDetailClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/privacy.html".toUri())
                    context.startActivity(intent)
                }
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

    AuthScaffold { innerPadding ->
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
                    state.emailError?.let { errorMessage ->
                        AniPickErrorText(errorMessage)
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AniPickLabeledField(
                        label = "비밀번호",
                        labelTrailingContent = {
                            AniPickValidationCheckIcon(isValid = state.isPasswordValid)
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
                                    AniPickPasswordVisibilityToggleIcon(
                                        showPassword = state.showPassword,
                                        onToggle = { onAction(EmailSignupAction.OnPasswordVisibilityToggle) },
                                    )
                                },
                                showPassword = state.showPassword,
                                maxLength = 20,
                            )
                        },
                    )
                    state.passwordError?.let { errorMessage ->
                        AniPickErrorText(errorMessage)
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
                        AniPickValidationCheckIcon(isValid = state.isAgreeAll)
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
                    state.termsError?.let { errorMessage ->
                        AniPickErrorText(errorMessage)
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
            AniPickValidationCheckIcon(isValid = checked)
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
