package com.jparkbro.core.ui.util

/** 빈 문자열이거나 "default.jpg" 플레이스홀더면 null로 치환 */
fun String?.orNullIfDefaultCover(): String? {
    if (isNullOrBlank()) return null
    if (contains("default.jpg")) return null
    return this
}
