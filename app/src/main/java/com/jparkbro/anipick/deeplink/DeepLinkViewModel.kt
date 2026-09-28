package com.jparkbro.anipick.deeplink

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 콜드/웜 스타트로 들어온 딥링크의 원본 Uri를 들고 있는다.
 * NavKey 변환은 로그인 판단이 끝난 시점에 app 모듈에서 수행하고, 그 값을 feature 쪽으로 넘기지 않는다.
 */
class DeepLinkViewModel : ViewModel() {

    private val _state = MutableStateFlow(DeepLinkState())
    val state: StateFlow<DeepLinkState> = _state.asStateFlow()

    /** MainActivity의 onCreate / onNewIntent에서 호출한다. */
    fun handleIntent(intent: Intent) {
        intent.data?.let { uri ->
            _state.update { it.copy(pendingUri = uri) }
        }
    }

    /** 딥링크 목적지로 네비게이션까지 끝낸 뒤 호출해 pending 상태를 비운다. */
    fun consumeDeepLink() {
        _state.update { it.copy(pendingUri = null) }
    }
}

data class DeepLinkState(
    val pendingUri: Uri? = null,
)
