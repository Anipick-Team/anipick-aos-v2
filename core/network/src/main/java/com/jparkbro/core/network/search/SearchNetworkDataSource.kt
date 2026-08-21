package com.jparkbro.core.network.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.search.dto.SearchAnimesResponse
import com.jparkbro.core.network.search.dto.SearchInitResponse
import com.jparkbro.core.network.search.dto.SearchPersonsResponse
import com.jparkbro.core.network.search.dto.SearchStudiosResponse

interface SearchNetworkDataSource {
    /** 검색 - 초기 화면(인기 검색어 등) - `GET /search/init`. */
    suspend fun getSearchInit(): Result<SearchInitResponse, DataError.Network>

    /** 검색 - 애니 탭 - `GET /search/animes`. */
    suspend fun getSearchAnimes(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
        page: Long = 1,
    ): Result<SearchAnimesResponse, DataError.Network>

    /** 검색 - 인물 탭 - `GET /search/persons`. */
    suspend fun getSearchPersons(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchPersonsResponse, DataError.Network>

    /** 검색 - 스튜디오 탭 - `GET /search/studios`. */
    suspend fun getSearchStudios(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchStudiosResponse, DataError.Network>
}
