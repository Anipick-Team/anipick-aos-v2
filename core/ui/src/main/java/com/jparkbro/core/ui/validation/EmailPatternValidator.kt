package com.jparkbro.core.ui.validation

import android.util.Patterns
import com.jparkbro.core.model.validation.PatternValidator

/** 이메일 형식 검증 */
object EmailPatternValidator: PatternValidator {

    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

}