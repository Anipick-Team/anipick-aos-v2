package com.jparkbro.auth.impl.password.verification

/** "인증번호 받기"/"재발송" 버튼 상태. 인증번호 자체 유효시간은 [PasswordVerificationState.codeExpiresInSeconds]가 별도로 관리한다. */
sealed interface VerificationCodeRequestState {
    data object Idle : VerificationCodeRequestState
    data object Loading : VerificationCodeRequestState
    data class Cooldown(val remainingSeconds: Int) : VerificationCodeRequestState
    data object Available : VerificationCodeRequestState
}
