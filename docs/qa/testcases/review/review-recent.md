# Review - 최근 리뷰 전체보기 (RecentReviewScreen)

패키지: `feature/review/impl/recent`. 근거 파일: `RecentReviewAction.kt`, `RecentReviewState.kt`, `RecentReviewViewModel.kt`, `RecentReviewScreen.kt`, `components/ReviewListContent.kt`. 홈 메인 "최근 리뷰" 더보기에서 진입한다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| REVIEW-RECENTREVIEW-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-02 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤 - `error`는 저장되지만 렌더링 안 함) | `OnLoadMore` / `RecentReviewViewModel.loadMore` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-03 | (자동) 화면 진입 - 최근 리뷰 피드 조회 | - | `getRecentReviewFeed()` 성공 → 리뷰 카드(`AniPickReviewCard`, 애니 정보+프로필 모두 표시) 목록 표시(로딩 중엔 스켈레톤) | - | - | `RecentReviewViewModel.loadReviews` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-03b | (자동) 화면 진입 | - | 위와 동일 | `getRecentReviewFeed()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 `RecentReviewScreen`은 렌더링하지 않음 | `RecentReviewViewModel.loadReviews` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-04 | 리뷰 카드 내 좋아요 하트 아이콘 | - | 낙관적 토글 - `state.reviews` 중 해당 리뷰의 색상+숫자를 즉시 변경한 뒤 `likeReview`/`unlikeReview` 호출 | 좋아요/좋아요 취소 API 실패 | 스낵바(API 에러 메시지) + 해당 리뷰만 색상+숫자 원상복구 | `RecentReviewViewModel.toggleLike` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-05 | 리뷰 카드 더보기(⋯) 메뉴 노출 | - | 내 리뷰(`isMine == true`)면 수정/삭제, 아니면 신고/차단 항목 노출 | - | - | `AniPickReviewCard`(`isMine` 분기) |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-06 | 리뷰 카드 이미지/타이틀 영역 | - | 애니 상세(`CatalogNavKey.Anime`)로 이동 | - | - | `OnAnimeClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-07 | 더보기 메뉴 - 수정(내 리뷰) | - | 리뷰 작성/수정 화면(`ReviewNavKey.Write(animeId)`)으로 이동 | - | - | `OnEditClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-08 | 더보기 메뉴 - 삭제(내 리뷰) | - | 삭제 확인 다이얼로그 표시 | - | - | `OnDeleteClick` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-09 | 삭제 확인 다이얼로그 - 삭제 | 다이얼로그 표시 중 | `deleteReview()` 성공 → 다이얼로그 닫힘 + 목록에서 해당 리뷰 제거 + 스낵바 "리뷰가 삭제되었습니다." | `deleteReview()` 실패 | 다이얼로그는 닫히고 스낵바(API 에러 메시지) - 목록은 그대로 유지 | `RecentReviewViewModel.onDeleteConfirm` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-10 | 삭제 확인 다이얼로그 - 취소/바깥 영역 | 다이얼로그 표시 중 | 다이얼로그 닫힘(`deleteTargetReviewId = null`) | - | - | `OnDeleteDismiss` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-11 | 더보기 메뉴 - 신고(남 리뷰) | - | 신고 다이얼로그 표시 | - | - | `OnReportClick` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-12 | 신고 다이얼로그 - 유형 선택 | 다이얼로그 표시 중 | 선택한 유형으로 갱신 | - | - | `OnReportCategorySelect` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-13 | 신고 다이얼로그 - 신고하기 | 유형 선택됨 | `reportReview()` 성공 → 다이얼로그 닫힘 + 스낵바 "신고가 접수되었습니다." | `reportReview()` 실패 | 다이얼로그는 닫히고 스낵바(API 에러 메시지) | `RecentReviewViewModel.onReportConfirm` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-14 | 신고 다이얼로그 - 닫기/바깥 영역 | 다이얼로그 표시 중 | 다이얼로그 닫힘(선택 유형 초기화) | - | - | `OnReportDismiss` |  |  |  |  |  |  |  |  |
| REVIEW-RECENTREVIEW-15 | 더보기 메뉴 - 차단(남 리뷰) | - | `blockUser()` 성공 → 스낵바 "차단되었습니다." | `blockUser()` 실패(이미 차단(130)/본인 차단(131) 포함) | 스낵바(API 에러 메시지 - 130/131은 서버가 노출 메시지를 내려주지 않음, `error-codes.md` 참고) | `RecentReviewViewModel.blockUser` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (REVIEW-RECENTREVIEW-03b)**.
- **좋아요/신고/차단은 이 화면(`RecentReviewScreen`)에서만 연동됨** - `AniPickReviewCard`를 쓰는 다른 화면(애니 상세 "리뷰" 탭 `AnimeReviewTabContent`)은 아직 콜백을 넘기지 않아 전부 기본값(no-op)이다. 수정/삭제는 이 화면과 [`review-rated.md`](./review-rated.md)(평가한 작품) 둘 다 연동됨.
