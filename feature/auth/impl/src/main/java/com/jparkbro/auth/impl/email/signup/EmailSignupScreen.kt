package com.jparkbro.auth.impl.email.signup

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.auth.impl.component.AuthFormLayout
import com.jparkbro.auth.impl.email.signup.components.EmailSignupAgreementSection
import com.jparkbro.auth.impl.email.signup.components.EmailSignupFields
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

    AuthFormLayout(
        title = "이메일 회원가입",
        subtitle = "회원 서비스 이용을 위해 회원가입 해주세요.",
        onBackClick = { onAction(EmailSignupAction.OnBackClick) },
        bottom = {
            EmailSignupAgreementSection(state = state, onAction = onAction)
        },
    ) {
        EmailSignupFields(
            state = state,
            onAction = onAction,
            passwordFocusRequester = passwordFocusRequester,
            focusManager = focusManager,
        )
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
