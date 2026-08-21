package com.jparkbro.core.model.validation

/** 값이 특정 패턴과 일치하는지 확인하는 인터페이스 */
interface PatternValidator {
    fun matches(value: String): Boolean
}