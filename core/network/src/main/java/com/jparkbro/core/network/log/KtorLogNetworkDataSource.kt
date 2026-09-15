package com.jparkbro.core.network.log

import com.jparkbro.core.network.constructRoute
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.utils.io.CancellationException

class KtorLogNetworkDataSource(
    private val httpClient: HttpClient,
) : LogNetworkDataSource {

    override suspend fun sendLog(url: String) {
        try {
            httpClient.get { url(constructRoute(url)) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
