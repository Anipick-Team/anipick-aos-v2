package com.jparkbro.community.impl.write

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.community.CommunityRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class CommunityWriteViewModel(
    seriesId: Long,
    postId: Long?,
    private val communityRepository: CommunityRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityWriteState(seriesId = seriesId, postId = postId))
    val state: StateFlow<CommunityWriteState> = _state.asStateFlow()

    private val _events = Channel<CommunityWriteEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeSubmitEnabled()
        if (postId != null) loadExistingPost(postId)
    }

    fun onAction(action: CommunityWriteAction) {
        when (action) {
            is CommunityWriteAction.Navigation -> Unit // Root에서 처리한다.
            CommunityWriteAction.OnSubmitClick -> onSubmitClick()
            is CommunityWriteAction.OnSpoilerToggle -> _state.update { it.copy(isSpoiler = action.isSpoiler) }
            is CommunityWriteAction.OnImagesAdd -> onImagesAdd(action.uris)
            is CommunityWriteAction.OnPhotoRemove -> {
                _state.update { it.copy(photos = it.photos.filterNot { photo -> photo.key == action.key }) }
            }
        }
    }

    private fun onImagesAdd(uris: List<Uri>) {
        val current = _state.value.photos
        val remaining = MAX_IMAGE_COUNT - current.size
        if (remaining <= 0) return
        val newPhotos = uris.take(remaining).map { CommunityWritePhoto.New(it) }
        _state.update { it.copy(photos = current + newPhotos) }
    }

    private fun observeSubmitEnabled() {
        viewModelScope.launch {
            combine(
                snapshotFlow { _state.value.titleState.text.toString() },
                snapshotFlow { _state.value.contentState.text.toString() },
            ) { title, content -> title.isNotBlank() && content.isNotBlank() }
                .distinctUntilChanged()
                .collect { isSubmitEnabled ->
                    _state.update { it.copy(isSubmitEnabled = isSubmitEnabled) }
                }
        }
    }

    /** 수정 모드 진입 시 기존 글 내용을 채운다 - 기존 첨부 이미지도 [CommunityWritePhoto.Existing]으로 채워서 유지한다 */
    private fun loadExistingPost(postId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            communityRepository.getPostDetail(postId)
                .onSuccess { post ->
                    post.title?.let { _state.value.titleState.setTextAndPlaceCursorAtEnd(it) }
                    post.content?.let { _state.value.contentState.setTextAndPlaceCursorAtEnd(it) }
                    val existingPhotos = post.imageIds.orEmpty().mapIndexed { index, imageId ->
                        CommunityWritePhoto.Existing(
                            imageId = imageId,
                            bytes = post.imageBytesList?.getOrNull(index),
                            url = post.imageUrls?.getOrNull(index),
                        )
                    }
                    _state.update {
                        it.copy(isSpoiler = post.isSpoiler == true, photos = existingPhotos, isLoading = false)
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
        }
    }

    private fun onSubmitClick() {
        val current = _state.value
        if (!current.isSubmitEnabled || current.isSubmitting) return

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            val existingImageIds = current.photos.filterIsInstance<CommunityWritePhoto.Existing>().map { it.imageId }
            val newUris = current.photos.filterIsInstance<CommunityWritePhoto.New>().map { it.uri }

            val uploadedImageIds = mutableListOf<Long>()
            for (uri in newUris) {
                val imageId = uploadImage(uri)
                if (imageId == null) {
                    _state.update { it.copy(isSubmitting = false) }
                    return@launch
                }
                uploadedImageIds += imageId
            }
            val imageIds = existingImageIds + uploadedImageIds

            val title = current.titleState.text.toString()
            val content = current.contentState.text.toString()

            val result = if (current.isEditMode) {
                communityRepository.updatePost(
                    postId = requireNotNull(current.postId),
                    title = title,
                    content = content,
                    isSpoiler = current.isSpoiler,
                    imageIds = imageIds,
                ).map { current.postId }
            } else {
                communityRepository.createPost(
                    seriesId = current.seriesId,
                    title = title,
                    content = content,
                    isSpoiler = current.isSpoiler,
                    imageIds = imageIds,
                )
            }

            result
                .onSuccess {
                    // 목록 화면(CommunityMain)이 다시 보일 때 최신 상태를 보여주도록 캐시를 재조회시킨다.
                    communityRepository.refreshCommunityBoardPosts()
                    globalSnackbarManager.showSnackbar(if (current.isEditMode) "게시글이 수정되었습니다." else "게시글이 등록되었습니다.")
                    _events.send(CommunityWriteEvent.WriteSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isSubmitting = false) }
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
        }
    }

    /** 이미지 1장을 업로드하고 imageId를 돌려준다 - 여러 장이면 [onSubmitClick]에서 이 함수를 순차 호출한다. */
    private suspend fun uploadImage(uri: Uri): Long? {
        val bytes = readBytes(uri)
        if (bytes == null) {
            globalSnackbarManager.showSnackbar("이미지를 불러오지 못했습니다.")
            return null
        }

        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"

        var uploadedId: Long? = null
        communityRepository.uploadPostImage(
            imageBytes = bytes,
            fileName = "community_post_image.$extension",
            mimeType = mimeType,
        )
            .onSuccess { uploadedId = it }
            .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        return uploadedId
    }

    private fun readBytes(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            Timber.e(e, "게시글 이미지 읽기 실패: $uri")
            null
        }
    }
}
