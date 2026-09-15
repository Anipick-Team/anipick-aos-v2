package com.jparkbro.catalog.impl.actor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.actor.ActorRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogActorViewModel(
    personId: Long,
    private val actorRepository: ActorRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogActorState(personId = personId))
    val state: StateFlow<CatalogActorState> = _state.asStateFlow()

    init {
        loadActorDetail(resetCursor = true)
    }

    fun onAction(action: CatalogActorAction) {
        when (action) {
            CatalogActorAction.OnLoadMore -> loadMore()
            CatalogActorAction.OnLikeClick -> toggleLike()
            is CatalogActorAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        loadActorDetail(resetCursor = false)
    }

    /** 찜 토글 - 낙관적으로 반영하고, 실패하면 원상복구 + 스낵바. */
    private fun toggleLike() {
        val wasLiked = _state.value.isLiked
        _state.update { it.copy(isLiked = !wasLiked) }

        viewModelScope.launch {
            val personId = _state.value.personId
            val result = if (wasLiked) actorRepository.unlikeActor(personId) else actorRepository.likeActor(personId)
            result.onFailure { error ->
                _state.update { it.copy(isLiked = wasLiked) }
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    private fun loadActorDetail(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId

            actorRepository.getActorDetail(
                personId = current.personId,
                lastId = lastId,
                size = PAGE_SIZE,
            )
                .onSuccess { page ->
                    val works = page.works ?: emptyList()
                    _state.update {
                        it.copy(
                            name = page.name,
                            profileImageUrl = page.profileImageUrl,
                            isLiked = page.isLiked ?: false,
                            count = page.count ?: 0,
                            works = if (resetCursor) works else it.works + works,
                            cursor = page.cursor,
                            endReached = works.size < PAGE_SIZE || page.cursor == null,
                            isLoading = false,
                            isLoadingMore = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.toDisplayMessage(), isLoading = false, isLoadingMore = false) }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 18
    }
}
