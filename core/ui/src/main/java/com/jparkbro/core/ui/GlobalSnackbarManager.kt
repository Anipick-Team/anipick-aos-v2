package com.jparkbro.core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 앱 전역에서 스낵바를 띄우기 위한 싱글턴. 화면 이동과 무관하게 앱 최상위(app 모듈)에서 한 곳만 collect해서
 * 보여주므로, 어떤 ViewModel에서 호출하든 현재 화면 위에 스낵바가 뜬다.
 * 메시지를 큐([messages])로 들고 있어서 연달아 여러 번 호출돼도 씹히지 않고 순서대로 하나씩 보여준다.
 */
class GlobalSnackbarManager {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun showSnackbar(message: String) {
        _messages.update { it + message }
    }

    /** 현재(맨 앞) 스낵바를 큐에서 제거한다. 스낵바가 다 보여지고 사라진 뒤 호출한다. */
    fun dismissCurrent() {
        _messages.update { it.drop(1) }
    }
}
