package com.jparkbro.core.network.character

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.character.dto.AnimeCharactersResponse

interface CharacterNetworkDataSource {
    /** 애니 캐릭터 목록 - `GET /animes/{animeId}/characters`. */
    suspend fun getAnimeCharacters(
        animeId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<AnimeCharactersResponse, DataError.Network>
}
