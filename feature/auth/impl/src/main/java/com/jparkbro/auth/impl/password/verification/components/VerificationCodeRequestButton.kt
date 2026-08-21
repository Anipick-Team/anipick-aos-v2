package com.jparkbro.auth.impl.password.verification.components

import androidx.compose.runtime.Composable
import com.jparkbro.auth.impl.password.verification.VerificationCodeRequestState
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.model.ButtonSize

@Composable
internal fun VerificationCodeRequestButton(
    requestState: VerificationCodeRequestState,
    isEmailValid: Boolean,
    onClick: () -> Unit,
) {
    val label = when (requestState) {
        VerificationCodeRequestState.Idle -> "인증번호 받기"
        VerificationCodeRequestState.Loading -> "전송 중..."
        is VerificationCodeRequestState.Cooldown -> {
            val minutes = requestState.remainingSeconds / 60
            val seconds = requestState.remainingSeconds % 60
            "전송됨 %d:%02d".format(minutes, seconds)
        }
        VerificationCodeRequestState.Available -> "재발송하기"
    }
    val isEnabled = isEmailValid &&
        (requestState is VerificationCodeRequestState.Idle || requestState is VerificationCodeRequestState.Available)

    AniPickButton(
        text = label,
        onClick = onClick,
        size = ButtonSize.S,
        enabled = isEnabled,
        isLoading = requestState is VerificationCodeRequestState.Loading,
    )
}
