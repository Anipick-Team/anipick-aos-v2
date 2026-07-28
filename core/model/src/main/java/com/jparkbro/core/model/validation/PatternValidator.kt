package com.jparkbro.core.model.validation

interface PatternValidator {
    fun matches(value: String): Boolean
}