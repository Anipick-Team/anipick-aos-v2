package com.jparkbro.core.data.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.search.SearchActorPage
import com.jparkbro.core.model.search.SearchAnimePage
import com.jparkbro.core.model.search.SearchCounts
import com.jparkbro.core.model.search.SearchStudioPage
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.search.SearchNetworkDataSource
import com.jparkbro.core.network.search.dto.toActor
import com.jparkbro.core.network.search.dto.toAnime
import com.jparkbro.core.network.search.dto.toStudio

class SearchRepositoryImpl(
    private val searchNetworkDataSource: SearchNetworkDataSource,
    private val recentSearchDataStore: RecentSearchDataStore,
) : SearchRepository {

    override val recentSearches = recentSearchDataStore.recentSearches

    override suspend fun getPopularAnimes(): Result<List<Anime>?, DataError.Network> {
        return searchNetworkDataSource.getSearchInit()
            .map { response -> response.popularAnimes?.map { it.toAnime() } }
    }

    override suspend fun getSearchAnimes(
        query: String,
        lastId: Long?,
        size: Int,
        page: Long,
    ): Result<SearchAnimePage, DataError.Network> {
        return searchNetworkDataSource.getSearchAnimes(
            query = query,
            lastId = lastId,
            size = size,
            page = page,
        ).map { response ->
            SearchAnimePage(
                animes = CursorPage(
                    cursor = response.cursor.toCursor(),
                    items = response.animes?.map { it.toAnime() },
                    count = response.count,
                ),
                counts = SearchCounts(
                    animeCount = response.count,
                    actorCount = response.personCount,
                    studioCount = response.studioCount,
                ),
                nextPage = response.nextPage,
            )
        }
    }

    override suspend fun getSearchActors(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchActorPage, DataError.Network> {
        return searchNetworkDataSource.getSearchPersons(
            query = query,
            lastId = lastId,
            size = size,
        ).map { response ->
            SearchActorPage(
                actors = CursorPage(
                    cursor = response.cursor.toCursor(),
                    items = response.persons?.map { it.toActor() },
                    count = response.count,
                ),
                counts = SearchCounts(
                    animeCount = response.animeCount,
                    actorCount = response.count,
                    studioCount = response.studioCount,
                ),
            )
        }
    }

    override suspend fun getSearchStudios(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchStudioPage, DataError.Network> {
        return searchNetworkDataSource.getSearchStudios(
            query = query,
            lastId = lastId,
            size = size,
        ).map { response ->
            SearchStudioPage(
                studios = CursorPage(
                    cursor = response.cursor.toCursor(),
                    items = response.studios?.map { it.toStudio() },
                    count = response.count,
                ),
                counts = SearchCounts(
                    animeCount = response.animeCount,
                    actorCount = response.personCount,
                    studioCount = response.count,
                ),
            )
        }
    }

    override suspend fun saveRecentSearch(query: String) = recentSearchDataStore.saveRecentSearch(query)

    override suspend fun removeRecentSearch(query: String) = recentSearchDataStore.removeRecentSearch(query)

    override suspend fun clearRecentSearches() = recentSearchDataStore.clearRecentSearches()
}
