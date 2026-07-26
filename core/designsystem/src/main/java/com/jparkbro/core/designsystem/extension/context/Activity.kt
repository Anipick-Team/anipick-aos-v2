package com.jparkbro.core.designsystem.extension.context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Context에서 Activity를 찾습니다.
 * ContextWrapper 체인을 따라가며 Activity를 찾습니다.
 *
 * @return Activity 또는 null
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

/**
 * Context에서 Activity를 찾습니다.
 * Activity를 찾지 못하면 예외를 던집니다.
 *
 * @return Activity
 * @throws IllegalStateException Activity를 찾을 수 없는 경우
 */
fun Context.requireActivity(): Activity {
    return findActivity() ?: error("Activity를 찾을 수 없습니다")
}
