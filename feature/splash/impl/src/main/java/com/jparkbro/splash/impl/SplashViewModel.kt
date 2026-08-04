package com.jparkbro.splash.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.common.CommonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

private const val SPLASH_MIN_DURATION_MILLIS = 1_500

class SplashViewModel(
    private val commonRepository: CommonRepository,
    private val tokenProvider: TokenProvider,
) : ViewModel() {

    private val _events = Channel<SplashEvent>()
    val events = _events.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            val minDurationJob = async { delay(SPLASH_MIN_DURATION_MILLIS.milliseconds) }

            fetchMetadata()
            val event = checkAutoLogin()

            minDurationJob.await()
            sendEvent(event)
        }
    }

    private suspend fun fetchMetadata() {
        commonRepository.getMetadata()
            .onSuccess { metadata ->
                Timber.d("메타데이터 조회 성공: $metadata")
            }
            .onFailure { error ->
                Timber.e("메타데이터 조회 실패: $error")
            }
    }

    private suspend fun checkAutoLogin(): SplashEvent {
        return if (tokenProvider.isRefreshTokenValid()) {
            SplashEvent.NavigateToHome
        } else {
            SplashEvent.NavigateToLogin
        }
    }

    private fun sendEvent(event: SplashEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
