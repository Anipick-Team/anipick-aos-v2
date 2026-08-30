package com.jparkbro.catalog.impl.anime

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeDetail
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.review.ReviewSort

data class CatalogAnimeState(
    val animeId: Long = 0L,
    val selectedTab: CatalogAnimeTab = CatalogAnimeTab.INFO,
    val isLoading: Boolean = false,
    val error: String? = null,

    // 작품정보 탭
    val animeDetail: AnimeDetail = AnimeDetail(animeId = 0L),
    val cast: List<AnimeCharacter> = emptyList(),
    val series: List<Anime> = emptyList(),
    val recommendations: List<Anime> = emptyList(),

    // 리뷰 탭 - [selectedTab]이 REVIEW로 처음 바뀔 때 지연 로딩된다.
    val hasLoadedReviews: Boolean = false,
    val myReview: Review = Review(),
    /** 별점 드래그 중인 값 - null이면 드래그 중이 아니라는 뜻이라 [myReview]의 확정된 값을 보여준다.
     *  API 성공 전까지는 [myReview]를 건드리지 않는다 - "상세 리뷰 작성하기" 활성화 여부가 [myReview]를 보기 때문. */
    val myReviewDraftRating: Float? = null,
    val reviewSort: ReviewSort = ReviewSort.LATEST,
    /** true면 스포일러로 표시된 리뷰도 함께 보여준다 - 기본은 숨김. */
    val showSpoilerReviews: Boolean = false,
    val reviews: List<Review> = emptyList(),
    /** 다음 페이지 요청용 커서. 마지막 페이지까지 불러왔으면 null. */
    val reviewsCursor: Cursor? = null,
    /** 마지막 페이지까지 다 불러왔는지 - true면 [reviewsCursor]가 있어도 더 요청하지 않는다. */
    val reviewsEndReached: Boolean = false,
    val isReviewsLoading: Boolean = false,
    /** 다음 페이지를 불러오는 중인지 - [isReviewsLoading]과 별개로 스켈레톤 없이 이어붙이는 로딩. */
    val isLoadingMoreReviews: Boolean = false,
    /** 신고 다이얼로그를 띄울 대상 리뷰 - null이면 다이얼로그를 띄우지 않는다. */
    val reportTargetReviewId: Long? = null,
    val reportCategory: ReportCategory? = null,
    /** 삭제 확인 다이얼로그를 띄울 대상 리뷰 - null이면 다이얼로그를 띄우지 않는다. */
    val deleteTargetReviewId: Long? = null,

    /** 커뮤니티 탭 - 게시판이 없으면(hasBoard == false) 생성 여부를 묻는 다이얼로그를 띄운다.
     *  게시판이 있으면 화면을 옮기지 않고 [CatalogAnimeEvent.NavigateToCommunity]로 다른 모듈로 이동한다. */
    val showCreateCommunityDialog: Boolean = false,
    /** 커뮤니티 탭 클릭 후 게시판 조회(`getCommunityBoardByAnime`) 중인지 - true인 동안 스켈레톤을 덮어 보여준다. */
    val isCommunityBoardLoading: Boolean = false,
)

enum class CatalogAnimeTab(val label: String) {
    INFO("작품정보"),
    REVIEW("리뷰"),
    COMMUNITY("커뮤니티"),
}
