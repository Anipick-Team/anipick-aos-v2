package com.jparkbro.core.data.util

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

/** 호출한 화면이 사라져 취소돼도 [block]은 끝까지 실행 - 서버 변경과 그 뒤 재조회를 한 묶음으로 보장 */
internal suspend fun <T> runUncancellable(block: suspend () -> T): T = withContext(NonCancellable) { block() }
