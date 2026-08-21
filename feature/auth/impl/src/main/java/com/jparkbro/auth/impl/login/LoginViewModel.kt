package com.jparkbro.auth.impl.login

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.jparkbro.auth.impl.BuildConfig
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnKakaoLoginClick -> { kakaoLogin(action.activity) }
            is LoginAction.OnGoogleLoginClick -> { googleLogin(action.activity) }
            LoginAction.OnEmailLoginClick,
            LoginAction.OnEmailSignupClick,
            LoginAction.OnProblemClick -> Unit
        }
    }

    private fun kakaoLogin(activity: Activity) {
        viewModelScope.launch {
            val token = requestKakaoToken(activity) ?: return@launch

            authRepository.loginWithKakao(token.accessToken)
                .onSuccess { reviewCompletedYn ->
                    _events.send(
                        if (reviewCompletedYn) LoginEvent.NavigateToHome else LoginEvent.NavigateToPreferenceSetup
                    )
                }
                .onFailure { error ->
                    Timber.e("카카오 로그인 실패: $error")
                    handleLoginFailure(error)
                }
        }
    }

    /** 카카오톡 앱 로그인을 우선 시도하고, 설치돼 있지 않거나 실패하면 카카오계정(웹) 로그인으로 대체한다. */
    private suspend fun requestKakaoToken(activity: Activity): OAuthToken? =
        suspendCancellableCoroutine { continuation ->
            val accountLoginCallback: (OAuthToken?, Throwable?) -> Unit = accountLoginCallback@{ token, error ->
                if (!continuation.isActive) return@accountLoginCallback

                if (error != null) {
                    Timber.e(error, "카카오계정 로그인 실패")
                    continuation.resume(null)
                } else {
                    continuation.resume(token)
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                    when {
                        !continuation.isActive -> Unit
                        error != null && error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                            // 사용자가 카카오톡 로그인 화면에서 직접 취소한 경우: 계정 로그인으로 넘어가지 않고 종료
                            continuation.resume(null)
                        }
                        error != null -> {
                            Timber.e(error, "카카오톡 로그인 실패, 카카오계정으로 재시도")
                            UserApiClient.instance.loginWithKakaoAccount(activity, callback = accountLoginCallback)
                        }
                        else -> continuation.resume(token)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(activity, callback = accountLoginCallback)
            }
        }

    private fun googleLogin(activity: Activity) {
        viewModelScope.launch {
            val idToken = requestGoogleIdToken(activity) ?: return@launch

            authRepository.loginWithGoogle(idToken)
                .onSuccess { reviewCompletedYn ->
                    _events.send(
                        if (reviewCompletedYn) LoginEvent.NavigateToHome else LoginEvent.NavigateToPreferenceSetup
                    )
                }
                .onFailure { error ->
                    Timber.e("구글 로그인 실패: $error")
                    handleLoginFailure(error)
                }
        }
    }

    private fun handleLoginFailure(error: DataError.Network) {
        globalSnackbarManager.showSnackbar(error.toDisplayMessage())
    }

    private suspend fun requestGoogleIdToken(activity: Activity): String? {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(serverClientId = BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        Timber.d("구글 로그인 시도: getCredential 요청")

        return try {
            val credential = CredentialManager.create(activity)
                .getCredential(context = activity, request = request)
                .credential

            Timber.d("구글 로그인: credential 수신 (type=${credential.type})")

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                GoogleIdTokenCredential.createFrom(credential.data).idToken
            } else {
                Timber.e("구글 로그인 실패: 알 수 없는 credential 타입 (${credential.type})")
                null
            }
        } catch (e: GetCredentialCancellationException) {
            // 사용자가 직접 취소한 경우
            Timber.d("구글 로그인 취소됨: ${e.type}, ${e.errorMessage}")
            null
        } catch (e: GetCredentialException) {
            Timber.e(e, "구글 로그인 실패: type=${e.type}, errorMessage=${e.errorMessage}")
            null
        }
    }
}
