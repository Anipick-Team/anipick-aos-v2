package com.jparkbro.core.data.character

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.pagination.CursorPage

/** 캐릭터 관련 데이터를 읽어오는 인터페이스 */
interface CharacterRepository {
    /** 캐릭터 전체 목록 - `GET /animes/{animeId}/characters`. */
    suspend fun getAnimeCharacters(
        animeId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<CursorPage<AnimeCharacter>, DataError.Network>
}
