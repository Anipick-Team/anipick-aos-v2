package com.jparkbro.core.network.image

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.image.dto.CommunityPostImageUploadResponse
import com.jparkbro.core.network.image.dto.UpdateProfileImageResponse
import com.jparkbro.core.network.postMultipart
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class KtorImageNetworkDataSource(
    private val httpClient: HttpClient,
) : ImageNetworkDataSource {

    override suspend fun updateProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<UpdateProfileImageResponse, DataError.Network> {
        return httpClient.postMultipart(
            route = "/image/profile-image",
            formData = formData {
                append(
                    key = "profileImageFile",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    },
                )
            },
        )
    }

    override suspend fun uploadCommunityPostImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<CommunityPostImageUploadResponse, DataError.Network> {
        return httpClient.postMultipart(
            route = "/image/community-post-image",
            formData = formData {
                append(
                    key = "postImageFile",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    },
                )
            },
        )
    }
}
