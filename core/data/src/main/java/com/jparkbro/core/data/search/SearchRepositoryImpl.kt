package com.jparkbro.core.data.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.search.SearchActorResult
import com.jparkbro.core.model.search.SearchAnimeResult
import com.jparkbro.core.model.search.SearchStudioResult
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
    ): Result<SearchAnimeResult, DataError.Network> {
        return searchNetworkDataSource.getSearchAnimes(
            query = query,
            lastId = lastId,
            size = size,
            page = page,
        ).map { response ->
            SearchAnimeResult(
                animes = response.animes?.map { it.toAnime() },
                animeCount = response.count,
                actorCount = response.personCount,
                studioCount = response.studioCount,
                cursor = response.cursor.toCursor(),
                nextPage = response.nextPage,
            )
        }
    }

    override suspend fun getSearchActors(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchActorResult, DataError.Network> {
        return searchNetworkDataSource.getSearchPersons(
            query = query,
            lastId = lastId,
            size = size,
        ).map { response ->
            SearchActorResult(
                actors = response.persons?.map { it.toActor() },
                actorCount = response.count,
                animeCount = response.animeCount,
                studioCount = response.studioCount,
                cursor = response.cursor.toCursor(),
            )
        }
    }

    override suspend fun getSearchStudios(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchStudioResult, DataError.Network> {
        return searchNetworkDataSource.getSearchStudios(
            query = query,
            lastId = lastId,
            size = size,
        ).map { response ->
            SearchStudioResult(
                studios = response.studios?.map { it.toStudio() },
                studioCount = response.count,
                animeCount = response.animeCount,
                actorCount = response.personCount,
                cursor = response.cursor.toCursor(),
            )
        }
    }

    override suspend fun getRecentSearches(): List<String> = recentSearchDataStore.getRecentSearches()

    override suspend fun saveRecentSearch(query: String) = recentSearchDataStore.saveRecentSearch(query)

    override suspend fun removeRecentSearch(query: String) = recentSearchDataStore.removeRecentSearch(query)

    override suspend fun clearRecentSearches() = recentSearchDataStore.clearRecentSearches()
}
