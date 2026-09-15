package com.jparkbro.catalog.impl.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.community.CommunityRepository
import com.jparkbro.core.data.rating.RatingRepository
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.review.ReviewSort
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogAnimeViewModel(
    animeId: Long,
    private val animeRepository: AnimeRepository,
    private val reviewRepository: ReviewRepository,
    private val ratingRepository: RatingRepository,
    private val communityRepository: CommunityRepository,
    private val userRepository: UserRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogAnimeState(animeId = animeId))
    val state: StateFlow<CatalogAnimeState> = _state.asStateFlow()

    private val _events = Channel<CatalogAnimeEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadAnimeInfo()
        loadMyReview()
        loadReviews(resetCursor = true)
    }

    fun onAction(action: CatalogAnimeAction) {
        when (action) {
            is CatalogAnimeAction.Navigation -> Unit // Root에서 처리한다.
            CatalogAnimeAction.OnLikeClick -> toggleLike()
            is CatalogAnimeAction.OnWatchStatusClick -> toggleWatchStatus(action.status)
            is CatalogAnimeAction.OnTabChanged -> onTabChanged(action.tab)
            is CatalogAnimeAction.OnReviewSortChanged -> onReviewSortChanged(action.sort)
            is CatalogAnimeAction.OnSpoilerToggle -> onSpoilerToggle(action.enabled)
            CatalogAnimeAction.OnLoadMoreReviews -> loadMoreReviews()
            CatalogAnimeAction.OnCreateCommunityDialogDismiss -> {
                _state.update { it.copy(showCreateCommunityDialog = false) }
            }
            CatalogAnimeAction.OnCreateCommunityConfirm -> Unit // TODO: 커뮤니티 생성 API가 추가되면 연동
            is CatalogAnimeAction.OnMyReviewRatingChange -> {
                _state.update { it.copy(myReviewDraftRating = action.rating) }
            }
            CatalogAnimeAction.OnMyReviewRatingChangeFinished -> submitMyReviewRating()
            is CatalogAnimeAction.OnReviewLikeClick -> toggleReviewLike(action.reviewId)
            is CatalogAnimeAction.OnReviewReportClick -> _state.update { it.copy(reportTargetReviewId = action.reviewId) }
            is CatalogAnimeAction.OnReviewReportCategorySelect -> _state.update { it.copy(reportCategory = action.category) }
            CatalogAnimeAction.OnReviewReportConfirm -> onReviewReportConfirm()
            CatalogAnimeAction.OnReviewReportDismiss -> dismissReviewReportDialog()
            is CatalogAnimeAction.OnReviewBlockClick -> blockReviewAuthor(action.userId)
            is CatalogAnimeAction.OnReviewDeleteClick -> _state.update { it.copy(deleteTargetReviewId = action.reviewId) }
            CatalogAnimeAction.OnReviewDeleteConfirm -> onReviewDeleteConfirm()
            CatalogAnimeAction.OnReviewDeleteDismiss -> dismissReviewDeleteDialog()
        }
    }

    /** 리뷰 목록 + 내 리뷰 좋아요 낙관적 토글 - 실패하면 해당 리뷰만 원상복구하고 스낵바를 띄운다. */
    private fun toggleReviewLike(reviewId: Long) {
        val current = _state.value
        val target = current.reviews.find { it.reviewId == reviewId }
            ?: current.myReview.takeIf { it.reviewId == reviewId }
            ?: return
        val wasLiked = target.isLiked == true
        applyReviewLikeState(reviewId, isLiked = !wasLiked)

        viewModelScope.launch {
            val result = if (wasLiked) reviewRepository.unlikeReview(reviewId) else reviewRepository.likeReview(reviewId)
            result.onFailure { error ->
                applyReviewLikeState(reviewId, isLiked = wasLiked)
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    private fun applyReviewLikeState(reviewId: Long, isLiked: Boolean) {
        fun Review.applyLike() = copy(isLiked = isLiked, likeCount = (likeCount ?: 0) + if (isLiked) 1 else -1)

        _state.update { state ->
            state.copy(
                reviews = state.reviews.map { review -> if (review.reviewId == reviewId) review.applyLike() else review },
                myReview = if (state.myReview.reviewId == reviewId) state.myReview.applyLike() else state.myReview,
            )
        }
    }

    private fun onReviewReportConfirm() {
        val reviewId = _state.value.reportTargetReviewId
        val category = _state.value.reportCategory
        dismissReviewReportDialog()
        if (reviewId == null || category == null) return

        viewModelScope.launch {
            reviewRepository.reportReview(reviewId, category)
                .onSuccess { globalSnackbarManager.showSnackbar("신고가 접수되었습니다.") }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissReviewReportDialog() {
        _state.update { it.copy(reportTargetReviewId = null, reportCategory = null) }
    }

    private fun blockReviewAuthor(userId: Long) {
        viewModelScope.launch {
            userRepository.blockUser(userId)
                .onSuccess { globalSnackbarManager.showSnackbar("차단되었습니다.") }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun onReviewDeleteConfirm() {
        val reviewId = _state.value.deleteTargetReviewId
        dismissReviewDeleteDialog()
        if (reviewId == null) return

        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            reviews = it.reviews.filterNot { review -> review.reviewId == reviewId },
                            myReview = if (it.myReview.reviewId == reviewId) Review() else it.myReview,
                            animeDetail = it.animeDetail.copy(
                                reviewCount = ((it.animeDetail.reviewCount ?: 1) - 1).coerceAtLeast(0),
                            ),
                        )
                    }
                    globalSnackbarManager.showSnackbar("리뷰가 삭제되었습니다.")
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissReviewDeleteDialog() {
        _state.update { it.copy(deleteTargetReviewId = null) }
    }

    /** 찜 토글 - 낙관적으로 반영하고, 실패하면 원상복구 + 스낵바. */
    private fun toggleLike() {
        val wasLiked = _state.value.animeDetail.isLiked == true
        _state.update { it.copy(animeDetail = it.animeDetail.copy(isLiked = !wasLiked)) }

        viewModelScope.launch {
            val result = if (wasLiked) animeRepository.unlikeAnime(_state.value.animeId) else animeRepository.likeAnime(_state.value.animeId)
            result.onFailure { error ->
                _state.update { it.copy(animeDetail = it.animeDetail.copy(isLiked = wasLiked)) }
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    /** 시청상태 토글(볼 애니/보는 중/다 본 애니) - 낙관적으로 반영하고, 실패하면 원상복구 + 스낵바.
     *  같은 상태를 다시 누르면 해제, 다른 상태를 누르면 그 상태로 전환. */
    private fun toggleWatchStatus(status: String) {
        val animeId = _state.value.animeId
        val previousStatus = _state.value.animeDetail.watchStatus
        val newStatus = if (previousStatus == status) null else status
        _state.update { it.copy(animeDetail = it.animeDetail.copy(watchStatus = newStatus)) }

        viewModelScope.launch {
            val result = when {
                previousStatus == null -> userRepository.addAnimeStatus(animeId, AnimeWatchStatus.valueOf(status))
                newStatus == null -> userRepository.deleteAnimeStatus(animeId)
                else -> userRepository.updateAnimeStatus(animeId, AnimeWatchStatus.valueOf(status))
            }
            result.onFailure { error ->
                _state.update { it.copy(animeDetail = it.animeDetail.copy(watchStatus = previousStatus)) }
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    /** 내 리뷰 별점 저장 - 아직 리뷰가 없으면(`reviewId == null`) `createRating`(POST /rating/{animeId}/animes,
     *  최초 등록), 이미 있는데 0f로 내리면 `deleteRating`(DELETE /rating/{reviewId}/animes, 삭제), 그 외
     *  이미 있으면 `updateRating`(PATCH /rating/{reviewId}/animes, 수정)로 분기한다. 리뷰도 없는데 0f면
     *  부를 API가 없어 그냥 드래그 값만 지운다. 성공해야 [CatalogAnimeState.myReview]에 반영된다
     *  ("상세 리뷰 작성하기" 활성화 여부가 이 값을 본다). 실패하면 스낵바 + 서버 값으로 재조회(드래그 이전 값으로 되돌아간다). */
    private fun submitMyReviewRating() {
        val current = _state.value
        val rating = current.myReviewDraftRating ?: return
        val reviewId = current.myReview.reviewId
        val isDelete = reviewId != null && rating <= 0f

        if (reviewId == null && rating <= 0f) {
            _state.update { it.copy(myReviewDraftRating = null) }
            return
        }

        viewModelScope.launch {
            val result = when {
                reviewId == null -> ratingRepository.createRating(current.animeId, rating)
                isDelete -> ratingRepository.deleteRating(reviewId)
                else -> ratingRepository.updateRating(reviewId, rating)
            }

            result
                .onSuccess {
                    // draftRating 해제와 myReview.rating 반영을 한 번에 묶음
                    _state.update {
                        it.copy(
                            myReviewDraftRating = null,
                            myReview = it.myReview.copy(rating = if (isDelete) null else rating),
                        )
                    }
                    globalSnackbarManager.showSnackbar(
                        when {
                            reviewId == null -> "평점을 등록했습니다."
                            isDelete -> "평점을 삭제했습니다."
                            else -> "평점을 수정했습니다."
                        }
                    )
                    if (reviewId == null || isDelete) {
                        // 신규 생성/삭제 후 reviewId 갱신을 위한 재조회
                        loadMyReview()
                    }
                }
                .onFailure { error ->
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                    _state.update { it.copy(myReviewDraftRating = null) }
                    loadMyReview()
                }
        }
    }

    /** "작품정보" 탭 4개 구간(상세 정보/출연진/시리즈/추천)을 동시에 조회 */
    private fun loadAnimeInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            awaitAll(
                async { loadAnimeDetail() },
                async { loadCast() },
                async { loadSeries() },
                async { loadRecommendations() },
            )

            _state.update { it.copy(isLoading = false) }
        }
    }

    /** 조회에 성공한 애니만 "최근 조회"로 남긴다 - 홈이 이 값으로 추천을 다시 불러오므로
     *  존재하지 않는 animeId를 저장하면 홈 추천이 빈 채로 굳는다. */
    private suspend fun loadAnimeDetail() {
        animeRepository.getAnimeDetailInfo(_state.value.animeId)
            .onSuccess { detail ->
                _state.update { it.copy(animeDetail = detail) }
                animeRepository.saveRecentAnimeId(_state.value.animeId)
            }
            .onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
    }

    private suspend fun loadCast() {
        animeRepository.getAnimeDetailCast(_state.value.animeId)
            .onSuccess { cast -> _state.update { it.copy(cast = cast) } }
            .onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
    }

    private suspend fun loadSeries() {
        animeRepository.getAnimeDetailSeries(_state.value.animeId)
            .onSuccess { series -> _state.update { it.copy(series = series) } }
            .onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
    }

    private suspend fun loadRecommendations() {
        animeRepository.getAnimeDetailRecommendations(_state.value.animeId)
            .onSuccess { recommendations -> _state.update { it.copy(recommendations = recommendations) } }
            .onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
    }

    private fun onTabChanged(tab: CatalogAnimeTab) {
        if (tab == CatalogAnimeTab.COMMUNITY) {
            loadCommunityBoard()
            return
        }
        if (_state.value.selectedTab == tab) return
        _state.update { it.copy(selectedTab = tab) }
    }

    /** 커뮤니티 게시판 조회
     *  게시판 있음: 커뮤니티 이동 이벤트, 없음: 생성 여부 다이얼로그 */
    private fun loadCommunityBoard() {
        viewModelScope.launch {
            _state.update { it.copy(isCommunityBoardLoading = true) }

            communityRepository.getCommunityBoardByAnime(_state.value.animeId)
                .onSuccess { board ->
                    _state.update { it.copy(isCommunityBoardLoading = false) }
                    if (board.hasBoard == true && board.seriesId != null) {
                        _events.send(CatalogAnimeEvent.NavigateToCommunity(board))
                    } else {
                        _state.update { it.copy(showCreateCommunityDialog = true) }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isCommunityBoardLoading = false) }
                    if (error is DataError.Network.Api && error.code == COMMUNITY_ANIME_NOT_FOUND_CODE) {
                        globalSnackbarManager.showSnackbar(error.reason ?: "애니 정보를 찾을 수 없습니다.")
                    } else {
                        globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                    }
                }
        }
    }

    private fun onReviewSortChanged(sort: ReviewSort) {
        if (_state.value.reviewSort == sort) return
        _state.update { it.copy(reviewSort = sort) }
        loadReviews(resetCursor = true)
    }

    private fun onSpoilerToggle(enabled: Boolean) {
        if (_state.value.showSpoilerReviews == enabled) return
        _state.update { it.copy(showSpoilerReviews = enabled) }
        loadReviews(resetCursor = true)
    }

    private fun loadMoreReviews() {
        val current = _state.value
        if (current.isReviewsLoading || current.isLoadingMoreReviews || current.reviewsEndReached) return
        loadReviews(resetCursor = false)
    }

    private fun loadMyReview() {
        viewModelScope.launch {
            reviewRepository.getMyReview(_state.value.animeId)
                .onSuccess { review -> _state.update { it.copy(myReview = review) } }
                .onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
        }
    }

    private fun loadReviews(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isReviewsLoading = true, error = null) else it.copy(isLoadingMoreReviews = true)
            }

            val lastId = if (resetCursor) null else current.reviewsCursor?.lastId
            val lastValue = if (resetCursor) null else current.reviewsCursor?.lastValue

            reviewRepository.getAnimeReviews(
                animeId = current.animeId,
                sort = current.reviewSort.apiValue,
                isSpoiler = if (current.showSpoilerReviews) null else false,
                lastId = lastId,
                lastValue = lastValue,
                size = PAGE_SIZE,
            )
                .onSuccess { page ->
                    val items = page.items ?: emptyList()
                    _state.update {
                        it.copy(
                            reviews = if (resetCursor) items else it.reviews + items,
                            reviewsCursor = page.cursor,
                            reviewsEndReached = items.size < PAGE_SIZE || page.cursor == null,
                            isReviewsLoading = false,
                            isLoadingMoreReviews = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(error = error.toDisplayMessage(), isReviewsLoading = false, isLoadingMoreReviews = false)
                    }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20

        /** "애니 찾을 수 없음" - 커뮤니티 게시판 조회 시 animeId가 유효하지 않을 때 반환되는 코드. */
        private const val COMMUNITY_ANIME_NOT_FOUND_CODE = 1001
    }
}
