package com.jparkbro.auth.impl.email.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jparkbro.auth.impl.email.signup.EmailSignupAction
import com.jparkbro.auth.impl.email.signup.EmailSignupState
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickValidationCheckIcon
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun EmailSignupAgreementSection(
    state: EmailSignupState,
    onAction: (EmailSignupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
