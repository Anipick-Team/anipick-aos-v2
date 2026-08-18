package com.jparkbro.core.model.user

enum class AuthProvider {
    LOCAL,
    KAKAO,
    GOOGLE,
}

fun AuthProvider?.snsLabel(): String = when (this) {
    AuthProvider.KAKAO -> "카카오톡"
    AuthProvider.GOOGLE -> "구글"
    AuthProvider.LOCAL -> ""
    null -> ""
}
