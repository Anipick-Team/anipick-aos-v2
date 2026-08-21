package com.jparkbro.core.ui.util

/** [word] 받침 유무로 목적격 조사 "을"/"를" 선택 */
fun objectParticleFor(word: String): String = if (hasBatchim(word)) "을" else "를"

/** [word] 받침 유무로 접속 조사 "과"/"와" 선택 */
fun withParticleFor(word: String): String = if (hasBatchim(word)) "과" else "와"

private fun hasBatchim(word: String): Boolean {
    val last = word.trim().lastOrNull() ?: return false
    val syllableIndex = last.code - 0xAC00
    if (syllableIndex !in 0..11171) return false
    return syllableIndex % 28 != 0
}
