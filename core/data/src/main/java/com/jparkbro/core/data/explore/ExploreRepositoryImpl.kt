package com.jparkbro.core.data.explore

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.explore.ExploreNetworkDataSource
import com.jparkbro.core.network.explore.dto.toAnime

class ExploreRepositoryImpl(
    private val exploreNetworkDataSource: ExploreNetworkDataSource,
) : ExploreRepository {

    override suspend fun getExploreAnimes(
        year: Int?,
        season: Int?,
        genres: List<Long>?,
        genreOp: String?,
        type: String?,
        sort: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<Anime>, DataError.Network> {
        return exploreNetworkDataSource.getExploreAnimes(
            year = year,
            season = season,
            genres = genres,
            genreOp = genreOp,
            type = type,
            sort = sort,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        ).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes?.map { it.toAnime() },
            )
        }
    }
}
