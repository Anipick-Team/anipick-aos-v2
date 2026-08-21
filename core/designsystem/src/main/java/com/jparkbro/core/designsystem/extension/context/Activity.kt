package com.jparkbro.core.designsystem.extension.context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/** ContextWrapper 체인을 따라가며 Activity를 찾는다. */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

/** Activity를 찾지 못하면 예외를 던진다. */
fun Context.requireActivity(): Activity {
    return findActivity() ?: error("Activity를 찾을 수 없습니다")
}
