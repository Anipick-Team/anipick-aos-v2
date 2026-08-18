package com.jparkbro.core.network.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.search.dto.SearchAnimesResponse
import com.jparkbro.core.network.search.dto.SearchInitResponse
import com.jparkbro.core.network.search.dto.SearchPersonsResponse
import com.jparkbro.core.network.search.dto.SearchStudiosResponse
import io.ktor.client.HttpClient

class KtorSearchNetworkDataSource(
    private val httpClient: HttpClient,
) : SearchNetworkDataSource {

    override suspend fun getSearchInit(): Result<SearchInitResponse, DataError.Network> {
        return httpClient.get("search/init")
    }

    override suspend fun getSearchAnimes(
        query: String,
        lastId: Long?,
        size: Int,
        page: Long,
    ): Result<SearchAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "search/animes",
            queryParameters = mapOf(
                "query" to query,
                "lastId" to lastId,
                "size" to size,
                "page" to page,
            ),
        )
    }

    override suspend fun getSearchPersons(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchPersonsResponse, DataError.Network> {
        return httpClient.get(
            route = "search/persons",
            queryParameters = mapOf(
                "query" to query,
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun getSearchStudios(
        query: String,
        lastId: Long?,
        size: Int,
    ): Result<SearchStudiosResponse, DataError.Network> {
        return httpClient.get(
            route = "search/studios",
            queryParameters = mapOf(
                "query" to query,
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }
}
