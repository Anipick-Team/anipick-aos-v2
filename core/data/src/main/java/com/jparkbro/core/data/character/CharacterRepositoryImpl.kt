package com.jparkbro.core.data.character

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.network.character.CharacterNetworkDataSource
import com.jparkbro.core.network.character.dto.toAnimeCharacter
import com.jparkbro.core.network.common.toCursor

class CharacterRepositoryImpl(
    private val characterNetworkDataSource: CharacterNetworkDataSource,
) : CharacterRepository {

    override suspend fun getAnimeCharacters(
        animeId: Long,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<AnimeCharacter>, DataError.Network> {
        return characterNetworkDataSource.getAnimeCharacters(
            animeId = animeId,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        ).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.characters?.map { it.toAnimeCharacter() },
            )
        }
    }
}
