package com.jparkbro.core.network.image

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.image.dto.CommunityPostImageUploadResponse
import com.jparkbro.core.network.image.dto.UpdateProfileImageResponse

interface ImageNetworkDataSource {
    /** 마이페이지 프로필 이미지 변경 - `POST /image/profile-image`. */
    suspend fun updateProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<UpdateProfileImageResponse, DataError.Network>

    /** 커뮤니티 게시글 이미지 업로드 (1장씩) - `POST /image/community-post-image`. */
    suspend fun uploadCommunityPostImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<CommunityPostImageUploadResponse, DataError.Network>
}
