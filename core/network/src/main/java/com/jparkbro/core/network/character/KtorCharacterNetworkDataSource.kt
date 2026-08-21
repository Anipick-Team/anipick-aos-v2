package com.jparkbro.core.network.character

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.character.dto.AnimeCharactersResponse
import com.jparkbro.core.network.get
import io.ktor.client.HttpClient

class KtorCharacterNetworkDataSource(
    private val httpClient: HttpClient,
) : CharacterNetworkDataSource {

    override suspend fun getAnimeCharacters(
        animeId: Long,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<AnimeCharactersResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/$animeId/characters",
            queryParameters = mapOf(
                "lastId" to lastId,
                "lastValue" to lastValue,
                "size" to size,
            ),
        )
    }
}
