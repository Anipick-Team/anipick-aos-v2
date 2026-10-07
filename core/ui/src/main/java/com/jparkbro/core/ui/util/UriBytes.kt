package com.jparkbro.core.ui.util

import android.content.Context
import android.net.Uri
import timber.log.Timber

/** [uri]의 내용을 바이트로 읽기. 읽기 실패 시 null */
fun Context.readBytesOrNull(uri: Uri): ByteArray? {
    return try {
        contentResolver.openInputStream(uri)?.use { it.readBytes() }
    } catch (e: Exception) {
        Timber.e(e, "이미지 읽기 실패: $uri")
        null
    }
}
