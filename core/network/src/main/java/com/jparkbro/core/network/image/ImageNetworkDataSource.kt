package com.jparkbro.core.network.image

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.image.dto.UpdateProfileImageResponse

interface ImageNetworkDataSource {
    suspend fun updateProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<UpdateProfileImageResponse, DataError.Network>
}
