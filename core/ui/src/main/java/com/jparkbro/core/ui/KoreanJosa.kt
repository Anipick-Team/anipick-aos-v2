package com.jparkbro.core.ui

/** [word]의 마지막 글자 받침 유무에 따라 목적격 조사 "을"/"를"을 고른다. 한글 음절이 아닌 글자로
 *  끝나면(영문 제목 등) 받침이 없는 것으로 보고 "를"을 쓴다. */
fun objectParticleFor(word: String): String {
    val last = word.trim().lastOrNull() ?: return "를"
    val syllableIndex = last.code - 0xAC00
    if (syllableIndex !in 0..11171) return "를"
    val hasBatchim = syllableIndex % 28 != 0
    return if (hasBatchim) "을" else "를"
}
