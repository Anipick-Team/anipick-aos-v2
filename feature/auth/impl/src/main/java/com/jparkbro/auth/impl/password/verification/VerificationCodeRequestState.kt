package com.jparkbro.auth.impl.password.verification

/**
 * "인증번호 받기"/"재발송" 버튼의 상태. API 통신을 시작하자마자 [Cooldown]으로 넘어가서 재전송을 30초간 막고,
 * 다 지나면 [Available]로 바뀌어 다시 클릭할 수 있다. 인증번호 자체의 유효시간(180초)은 이 상태가 아니라
 * [PasswordVerificationState.codeExpiresInSeconds]가 별도로 표시한다.
 */
sealed interface VerificationCodeRequestState {
    data object Idle : VerificationCodeRequestState
    data object Loading : VerificationCodeRequestState
    data class Cooldown(val remainingSeconds: Int) : VerificationCodeRequestState
    data object Available : VerificationCodeRequestState
}
