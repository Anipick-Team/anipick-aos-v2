package com.jparkbro.core.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** 동적 문자열과 문자열 리소스를 함께 다루기 위한 타입 */
sealed interface UiText {

    data class DynamicString(val value: String): UiText
    class StringResource(
        @param: StringRes val resId: Int,
        vararg val args: Any,
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)

        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}