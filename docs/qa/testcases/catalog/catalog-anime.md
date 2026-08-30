# Catalog - 애니 상세 (CatalogAnimeScreen)

패키지: `feature/catalog/impl/anime`. 근거 파일: `CatalogAnimeAction.kt`, `CatalogAnimeState.kt`, `CatalogAnimeViewModel.kt`, `CatalogAnimeScreen.kt`, `components/AnimeHeroSection.kt`, `components/AnimeStickyTabHeader.kt`, `components/AnimeInfoTabContent.kt`, `components/AnimeReviewTabContent.kt`.

작품정보/리뷰/커뮤니티 3개 탭(`CatalogAnimeTab`)이 한 화면에 있다. 탭 전환 자체는 `OnTabChanged`를 거치지만, COMMUNITY 탭은 다른 화면으로 이동/다이얼로그 트리거이지 실제 탭 콘텐츠가 없다(`CatalogAnimeTab.COMMUNITY -> Unit`).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGANIME-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-02 | 공유 아이콘 | - | 시스템 공유 시트(`Intent.ACTION_SEND`, 텍스트: 애니 제목) 표시 | - | - | `CatalogAnimeRoot`(`OnShareClick`) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-03 | 찜(좋아요) 아이콘 | - | 낙관적 토글 - `state.animeDetail.isLiked` 즉시 반전 후 `likeAnime`/`unlikeAnime` 호출 | `likeAnime`/`unlikeAnime` 실패 | 스낵바(API 에러 메시지) + `isLiked` 원상복구 | `CatalogAnimeViewModel.toggleLike` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-04 | 배너 이미지 탭 | - | 미구현 - 아무 동작 없음 | - | - | `OnBannerImageClick`(TODO 주석) |  | 이미지 상세보기 화면 대기 | | | | | | |
| CATALOG-CATALOGANIME-05 | 커버 이미지 탭 | - | 미구현 - 아무 동작 없음 | - | - | `OnCoverImageClick`(TODO 주석) |  | 위와 동일 사유 | | | | | | |
| CATALOG-CATALOGANIME-06 | 제작사 칩 클릭 | - | 스튜디오 상세 화면으로 이동 | - | - | `OnStudioClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-07 | 시청 상태 변경(볼 애니/보는 중/다 본 애니 버튼) | - | 낙관적 토글 - 다른 상태 클릭 시 그 상태로, 같은 상태 재클릭 시 해제로 즉시 반영 후 상태 전이에 맞게 `addAnimeStatus`(기존 없음)/`updateAnimeStatus`(다른 상태로 전환)/`deleteAnimeStatus`(해제) 호출 | 위 API 호출 실패 | 스낵바(API 에러 메시지) + `watchStatus` 원상복구 | `CatalogAnimeViewModel.toggleWatchStatus` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-08 | 탭 전환(작품정보/리뷰) | 현재 탭이 클릭한 탭이 아님 | 해당 탭으로 전환. 리뷰 탭은 처음 전환될 때만(`!hasLoadedReviews`) 내 리뷰 + 리뷰 목록을 지연 로딩 | - | - | `OnTabChanged` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-09 | 탭 전환(커뮤니티) | - | 실제 탭 전환 없이 `loadCommunityBoard()` 호출 - 조회 중엔 화면 전체를 덮는 로딩 화면(`CommunityBoardSkeleton` - 스피너 + "커뮤니티 확인 중..." 문구, 콘텐츠 모양을 예고하는 스켈레톤 아님) 표시, 게시판 있으면(`hasBoard && seriesId != null`) 커뮤니티 화면으로 바로 이동, 없으면 로딩 화면이 사라지고 생성 안내 다이얼로그 표시 | - | - | `OnTabChanged`(COMMUNITY 분기) / `isCommunityBoardLoading` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-09b | 탭 전환(커뮤니티) | 위와 동일 | 위와 동일 | `getCommunityBoardByAnime` 실패 - 애니 정보 못 찾음(`Api(code=1001)`) | 로딩 화면 사라짐 + 스낵바(서버 메시지, 없으면 "애니 정보를 찾을 수 없습니다.") | `CatalogAnimeViewModel.loadCommunityBoard` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-09c | 탭 전환(커뮤니티) | 위와 동일 | 위와 동일 | 그 외 실패(네트워크/Api/알 수 없음) | 로딩 화면 사라짐 + 스낵바(`toDisplayMessage()`) | `CatalogAnimeViewModel.loadCommunityBoard` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-10 | 정렬 드롭다운(최신순/좋아요 순/평가 높은 순/평가 낮은 순) | 리뷰 탭 | 선택한 정렬로 첫 페이지부터 재조회 | - | - | `OnReviewSortChanged` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-11 | "스포일러" 스위치 | 리뷰 탭 | 켜면 스포일러 리뷰 포함(`isSpoiler=null`), 끄면 제외(`isSpoiler=false`)해서 첫 페이지부터 재조회 | - | - | `OnSpoilerToggle` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-12 | 목록 스크롤 중 하단 도달(무한 스크롤) | 리뷰 탭, 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤 - `state.error`는 저장되지만 화면 어디서도 안 씀) | `OnLoadMoreReviews` / `CatalogAnimeViewModel.loadMoreReviews` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-13 | "상세 리뷰 작성하기" 버튼(내 리뷰 요약 박스) | 리뷰 탭, `myReview.rating != null`(평점을 매긴 적 있음) | 리뷰 작성/수정 화면으로 이동 | - | - | `OnWriteReviewClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-13b | "상세 리뷰 작성하기" 버튼 | 리뷰 탭, `myReview.rating == null`(아직 평점 없음) | 버튼 비활성화 - 클릭 불가 | - | - | `AnimeReviewTabContent`(`enabled = review.rating != null`) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-14 | 커뮤니티 생성 안내 다이얼로그 - "닫기" | 다이얼로그 표시됨 | 다이얼로그만 닫힘 | - | - | `OnCreateCommunityDialogDismiss` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-15 | 커뮤니티 생성 안내 다이얼로그 - "문의하기" | 다이얼로그 표시됨 | 미구현 - 아무 동작 없음 | - | - | `OnCreateCommunityConfirm`(TODO 주석) |  | 실제 문의 페이지로 연결되는 URL/화면이 아직 없음 | | | | | | |
| CATALOG-CATALOGANIME-16 | (자동) 화면 진입 - 작품정보 4구간(상세정보/출연진/시리즈/추천) 동시 조회 | - | `getAnimeDetailInfo`/`getAnimeDetailCast`/`getAnimeDetailSeries`/`getAnimeDetailRecommendations` 병렬 성공 → 작품정보 탭 표시(로딩 중엔 스켈레톤) | - | - | `CatalogAnimeViewModel.loadAnimeInfo` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-16b | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 4구간 중 하나 이상 실패 | UI 반응 없음 - `state.error`에 저장되지만 `CatalogAnimeScreen`이 렌더링하지 않음(스낵바/인라인/재시도 없음). 실패한 구간만 빈 상태로(예: 출연진 실패 시 출연진 섹션만 빈 리스트) 나머지는 정상 표시 | `CatalogAnimeViewModel.loadAnimeDetail`/`loadCast`/`loadSeries`/`loadRecommendations` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-17 | (자동) 리뷰 탭 첫 진입 - 내 리뷰 + 리뷰 목록 조회 | 리뷰 탭 첫 전환 | `getMyReview`/`getAnimeReviews` 성공 → 내 리뷰 요약 박스 + 리뷰 목록 표시 | - | - | `CatalogAnimeViewModel.loadMyReview` / `loadReviews` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-17b | (자동) 리뷰 탭 첫 진입 | 위와 동일 | 위와 동일 | `getMyReview` 또는 `getAnimeReviews` 실패 | UI 반응 없음 - `state.error`만 갱신되고 렌더링 안 함(CATALOG-CATALOGANIME-16b와 동일 패턴) | `CatalogAnimeViewModel.loadMyReview`/`loadReviews` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-18 | (표시 확인, 결과 없음) 리뷰 0건 | 리뷰 탭, 조회 성공했지만 `reviews`가 빈 리스트 | "아직 작성된 리뷰가 없습니다." 안내(`AniPickEmptyState`) 표시 | - | - | `animeReviewTabContent`(`reviews.isEmpty()` 분기) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-19 | 내 리뷰 요약 박스 - 별점 클릭/드래그 후 손 뗌, 아직 리뷰 없음(`myReview.reviewId == null`) | 리뷰 탭 | 드래그 중엔 `myReviewDraftRating`만 갱신(별점만 시각적으로 따라감, `myReview`/버튼 활성화 상태는 그대로) - 손을 떼면 `ratingRepository.createRating(animeId, rating)`(`POST /rating/{animeId}/animes`) 호출, 성공해야 `getMyReview()` 재조회로 `myReview`(신규 `reviewId` 포함) 갱신 → "상세 리뷰 작성하기" 버튼 활성화 | `createRating` 실패 | 스낵바(API 에러 메시지) + `getMyReview()` 재조회로 별점 원상복구(리뷰 없는 상태 유지, 버튼 계속 비활성화) | `CatalogAnimeViewModel.submitMyReviewRating` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-19b | 내 리뷰 요약 박스 - 별점 클릭/드래그 후 손 뗌, 이미 리뷰 있음(`myReview.reviewId != null`), 0보다 큰 값으로 | 리뷰 탭 | 손을 떼면 `ratingRepository.updateRating(reviewId, rating)`(`PATCH /rating/{reviewId}/animes`) 호출, 성공하면 `myReview.rating`만 즉시 갱신 | `updateRating` 실패 | 스낵바(API 에러 메시지) + `getMyReview()` 재조회로 별점 원상복구 | `CatalogAnimeViewModel.submitMyReviewRating` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-19c | 내 리뷰 요약 박스 - 별점 드래그로 0f까지 내린 뒤 손 뗌, 이미 리뷰 있음 | 리뷰 탭 | `ratingRepository.deleteRating(reviewId)`(`DELETE /rating/{reviewId}/animes`) 호출, 성공하면 `getMyReview()` 재조회로 `myReview` 초기화(리뷰 없는 상태) → "상세 리뷰 작성하기" 버튼 비활성화 | `deleteRating` 실패 | 스낵바(API 에러 메시지) + `getMyReview()` 재조회로 별점 원상복구(삭제 안 됨) | `CatalogAnimeViewModel.submitMyReviewRating` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-19d | 내 리뷰 요약 박스 - 별점 드래그로 0f까지 내린 뒤 손 뗌, 애초에 리뷰 없음 | 리뷰 탭 | 부를 API가 없어 아무 요청도 안 하고 드래그 값만 정리 | - | - | `CatalogAnimeViewModel.submitMyReviewRating`(`reviewId == null && rating <= 0f` 분기) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-20 | 리뷰 목록 카드 내 좋아요 하트 아이콘 | 리뷰 탭 | 낙관적 토글 - `state.reviews` 중 해당 리뷰의 색상+숫자 즉시 변경 후 `likeReview`/`unlikeReview` 호출 | 좋아요/좋아요 취소 API 실패 | 스낵바(API 에러 메시지) + 해당 리뷰만 색상+숫자 원상복구 | `CatalogAnimeViewModel.toggleReviewLike` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-21 | 리뷰 목록 카드 더보기(⋯) 메뉴 노출 | 리뷰 탭 | 내 리뷰(`isMine == true`)면 수정/삭제, 아니면 신고/차단 항목 노출 | - | - | `AniPickReviewCard`(`isMine` 분기) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-22 | 리뷰 목록 카드 더보기 - 수정(내 리뷰) | 리뷰 탭 | 리뷰 작성/수정 화면으로 이동(`OnWriteReviewClick` 재사용 - 이 화면은 애니 하나만 다루므로 어떤 카드든 목적지는 동일) | - | - | `AnimeReviewTabContent`(`onReviewEditClick`) (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-23 | 리뷰 목록 카드 더보기 - 삭제(내 리뷰) | 리뷰 탭 | 삭제 확인 다이얼로그 표시 | - | - | `OnReviewDeleteClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-24 | 삭제 확인 다이얼로그 - 삭제 | 다이얼로그 표시 중 | `deleteReview()` 성공 → 다이얼로그 닫힘 + 목록에서 해당 리뷰 제거 + `animeDetail.reviewCount` 1 감소 + 스낵바 "리뷰가 삭제되었습니다." | `deleteReview()` 실패 | 다이얼로그는 닫히고 스낵바(API 에러 메시지) - 목록은 그대로 유지 | `CatalogAnimeViewModel.onReviewDeleteConfirm` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-25 | 삭제 확인 다이얼로그 - 취소/바깥 영역 | 다이얼로그 표시 중 | 다이얼로그 닫힘(`deleteTargetReviewId = null`) | - | - | `OnReviewDeleteDismiss` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-26 | 리뷰 목록 카드 더보기 - 신고(남 리뷰) | 리뷰 탭 | 신고 다이얼로그 표시 | - | - | `OnReviewReportClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-27 | 신고 다이얼로그 - 유형 선택 | 다이얼로그 표시 중 | 선택한 유형으로 갱신 | - | - | `OnReviewReportCategorySelect` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-28 | 신고 다이얼로그 - 신고하기 | 유형 선택됨 | `reportReview()` 성공 → 다이얼로그 닫힘 + 스낵바 "신고가 접수되었습니다." | `reportReview()` 실패 | 다이얼로그는 닫히고 스낵바(API 에러 메시지) | `CatalogAnimeViewModel.onReviewReportConfirm` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-29 | 신고 다이얼로그 - 닫기/바깥 영역 | 다이얼로그 표시 중 | 다이얼로그 닫힘(선택 유형 초기화) | - | - | `OnReviewReportDismiss` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGANIME-30 | 리뷰 목록 카드 더보기 - 차단(남 리뷰) | 리뷰 탭 | `blockUser()` 성공 → 스낵바 "차단되었습니다." | `blockUser()` 실패(이미 차단(130)/본인 차단(131) 포함) | 스낵바(API 에러 메시지 - 130/131은 서버가 노출 메시지를 내려주지 않음, `error-codes.md` 참고) | `CatalogAnimeViewModel.blockReviewAuthor` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **작품정보/리뷰 로딩 실패 시 사용자 피드백이 전혀 없음 (CATALOG-CATALOGANIME-16b/17b)**: 이 화면의 `loadAnimeInfo`/`loadMyReview`/`loadReviews` 계열 `.onFailure`가 `state.error`에 저장할 뿐, `CatalogAnimeScreen`은 `state.error`를 어디에서도 읽지 않는다. 다른 화면들에 적용된 `AniPickEmptyState`/스낵바 재시도 패턴이 여기(작품정보/내 리뷰/리뷰 목록 3곳 모두)엔 없다. 실기기에서 네트워크 문제가 있으면 그냥 빈 섹션만 보이고 사용자는 원인을 알 수 없다. (찜/시청상태/별점은 이번에 연동되어 실패 시 스낵바가 뜨지만, 이 항목은 별개로 여전히 미해결.)
- **이미지 상세보기 미구현 (CATALOG-CATALOGANIME-04/05)**.
- **"문의하기" 목적지 없음 (CATALOG-CATALOGANIME-15)**: `OnCreateCommunityConfirm`이 TODO. 실제 문의 흐름/URL이 정해지면 연동 필요(Setting의 `OnContactClick`처럼 `Intent.ACTION_VIEW` 패턴 재사용 가능).
- **`ReviewSort.apiValue`/스포일러 필터 값 미검증**: `ReviewSort` 문자열 값과 `isSpoiler` 파라미터 의미(꺼짐=제외/켜짐=전체포함)는 이번 세션에서 추정한 값 - 실제 백엔드 동작과 다를 수 있음.
- **내 리뷰 요약 박스(`myReview`)와 리뷰 목록(`reviews`)이 별개 상태**: `getAnimeReviews`가 내 리뷰도 같이 내려주는 경우, 목록 카드에서 내 리뷰를 삭제해도 상단 요약 박스의 `myReview`는 갱신되지 않는다(반대로 상단 별점을 바꿔도 목록 쪽 카드는 안 바뀜) - 리뷰를 두 번 로드하는 구조라 동기화가 안 되어 있음.
