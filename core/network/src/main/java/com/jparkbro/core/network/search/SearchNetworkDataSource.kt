package com.jparkbro.core.network.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.search.dto.SearchAnimesResponse
import com.jparkbro.core.network.search.dto.SearchInitResponse
import com.jparkbro.core.network.search.dto.SearchPersonsResponse
import com.jparkbro.core.network.search.dto.SearchStudiosResponse

interface SearchNetworkDataSource {
    suspend fun getSearchInit(): Result<SearchInitResponse, DataError.Network>

    suspend fun getSearchAnimes(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
        page: Long = 1,
    ): Result<SearchAnimesResponse, DataError.Network>

    suspend fun getSearchPersons(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchPersonsResponse, DataError.Network>

    suspend fun getSearchStudios(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchStudiosResponse, DataError.Network>
}
