package com.jparkbro.core.ui.validation

object PasswordPatternValidator {

    private const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_PASSWORD_LENGTH = 16

    fun isValidPassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasMaxLength = password.length <= MAX_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasMaxLength = hasMaxLength,
            hasDigit = hasDigit,
            hasSpecialChar = hasSpecialChar
        )
    }
}

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasMaxLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false,
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasMaxLength && hasDigit && hasSpecialChar
}