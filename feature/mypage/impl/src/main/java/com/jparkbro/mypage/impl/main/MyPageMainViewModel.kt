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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        collectMyPageProfile()
        initDataLoad()
    }

    /** [UserRepository.myPageProfile] 구독 - 값 변경 시 자동 반영, 프로필 이미지 바이트도 함께 내려옴 */
    private fun collectMyPageProfile() {
        userRepository.myPageProfile
            .onEach { profile ->
                _state.update {
                    it.copy(
                        nickname = profile?.nickname,
                        profileImageUrl = profile?.profileImageUrl,
                        profileImageBytes = profile?.profileImageBytes,
                        watchCounts = profile?.watchCounts ?: WatchCounts(),
                        likedAnimes = profile?.likedAnimes ?: emptyList(),
                        likedPersons = profile?.likedPersons ?: emptyList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /** 캐시 있으면 그대로 쓰고, 없을 때만(최초 진입) 서버에서 불러온다 - 실제 state 반영은 [collectMyPageProfile]이 한다. */
    private fun initDataLoad() {
        viewModelScope.launch {
            val start = System.currentTimeMillis()
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.loadMyPage()
                .onSuccess {
                    Timber.d("[MyPageLoad] initDataLoad 총 ${System.currentTimeMillis() - start}ms")
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    Timber.d("[MyPageLoad] initDataLoad 실패 ${System.currentTimeMillis() - start}ms error=$error")
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

    private fun changeProfileImage(image: Uri) {
        viewModelScope.launch {
            val imageBytes = readBytes(image) ?: return@launch
            val mimeType = context.contentResolver.getType(image) ?: "image/jpeg"
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"

            userRepository.updateProfileImage(
                imageBytes = imageBytes,
                fileName = "profile_image.$extension",
                mimeType = mimeType,
            )
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
