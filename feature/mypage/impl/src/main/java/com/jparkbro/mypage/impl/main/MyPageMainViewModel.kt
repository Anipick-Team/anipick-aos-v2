package com.jparkbro.mypage.impl.main

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.mypage.WatchCounts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MyPageMainViewModel(
    private val userRepository: UserRepository,
    private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageMainState())
    val state: StateFlow<MyPageMainState> = _state.asStateFlow()

    init {
        loadMyPage()
    }

    private fun loadMyPage() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.getMyPage()
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            nickname = profile.nickname,
                            profileImageUrl = profile.profileImageUrl,
                            watchCounts = profile.watchCounts ?: WatchCounts(),
                            likedAnimes = profile.likedAnimes ?: emptyList(),
                            likedPersons = profile.likedPersons ?: emptyList(),
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                }
        }
    }

    fun onAction(action: MyPageMainAction) {
        when (action) {
            is MyPageMainAction.OnChangeProfileImage -> changeProfileImage(action.image)
            is MyPageMainAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    /** 업로드 성공 시 기존 URL의 마지막 경로 조각(id)만 새 imageId로 교체, 기존 URL 없으면 새로 불러옴 */
    private fun changeProfileImage(image: Uri) {
        viewModelScope.launch {
            val imageBytes = readBytes(image) ?: return@launch

            val mimeType = context.contentResolver.getType(image) ?: "image/jpeg"
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"

            userRepository.updateProfileImage(
                imageBytes = imageBytes,
                fileName = "profile_image.$extension",
                mimeType = mimeType,
            ).onSuccess { imageId ->
                val currentUrl = _state.value.profileImageUrl
                if (currentUrl == null) {
                    loadMyPage()
                } else {
                    _state.update {
                        it.copy(profileImageUrl = currentUrl.substringBeforeLast('/') + "/" + imageId)
                    }
                }
            }
        }
    }

    private fun readBytes(image: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(image)?.use { it.readBytes() }
        } catch (e: Exception) {
            Timber.e(e, "프로필 이미지 읽기 실패: $image")
            null
        }
    }
}
