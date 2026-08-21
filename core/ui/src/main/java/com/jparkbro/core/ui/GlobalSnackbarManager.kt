package com.jparkbro.core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** 앱 전역 스낵바 큐 관리 싱글턴 */
class GlobalSnackbarManager {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    /** 표시 대기 중인 스낵바 메시지 큐 */
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun showSnackbar(message: String) {
        _messages.update { it + message }
    }

    /** 현재(맨 앞) 스낵바를 큐에서 제거 */
    fun dismissCurrent() {
        _messages.update { it.drop(1) }
    }
}
