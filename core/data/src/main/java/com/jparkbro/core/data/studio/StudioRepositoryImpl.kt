package com.jparkbro.core.data.studio

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.studio.StudioAnimePage
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.studio.StudioNetworkDataSource
import com.jparkbro.core.network.studio.dto.toAnime

class StudioRepositoryImpl(
    private val studioNetworkDataSource: StudioNetworkDataSource,
) : StudioRepository {

    override suspend fun getStudioAnimes(
        studioId: Long,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<StudioAnimePage, DataError.Network> {
        return studioNetworkDataSource.getStudioAnimes(
            studioId = studioId,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        ).map { response ->
            StudioAnimePage(
                studioName = response.studioName,
                cursor = response.cursor.toCursor(),
                animes = response.animes?.map { it.toAnime() },
            )
        }
    }
}
