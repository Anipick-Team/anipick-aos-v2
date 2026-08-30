# Review - 평가한 작품 전체보기 (RatedReviewScreen)

패키지: `feature/review/impl/rated`. 근거 파일: `RatedReviewAction.kt`, `RatedReviewState.kt`, `RatedReviewViewModel.kt`, `RatedReviewScreen.kt`, `components/RatedReviewContent.kt`. 마이페이지 메인 "평가한 작품" 더보기에서 진입한다(`docs/qa/testcases/mypage/mypage-main.md`의 `MYPAGE-MYPAGEMAIN-06` 참고). 원래 `feature/mypage/impl/detail`의 `MyPageDetailType.RatedAnimes`였던 화면을 review 모듈로 이전한 것 — 옛 케이스는 `docs/qa/testcases/mypage/mypage-detail.md`에 "이동됨" 표시만 남아 있다. 리스트는 전부 내 리뷰라 카드 더보기 메뉴에 수정/삭제만 뜬다(`RatedAnimeResponse.toReview()`에서 `isMine = true` 고정).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| REVIEW-RATEDREVIEW-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-02 | 리뷰 카드 이미지/타이틀 영역 | - | 애니 상세(`CatalogNavKey.Anime`)로 이동 | - | - | `OnAnimeClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-03 | 더보기 메뉴 - 수정 | - | 리뷰 작성/수정 화면(`ReviewNavKey.Write(animeId)`)으로 이동 | - | - | `OnEditClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-04 | 정렬 드롭다운(최신순/좋아요 순/평가 높은 순/평가 낮은 순) | - | 선택한 정렬 기준으로 첫 페이지부터 재조회(`ratedSort` 변경 + `load(resetCursor=true)`) | - | - | `OnRatedSortSelected` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-05 | "리뷰만 보기" 스위치 | - | 재조회 없이 `reviewOnly` 상태만 토글 - 이미 불러온 목록을 클라이언트에서 필터링(`content`가 빈 값인 항목 숨김)해서 즉시 보여준다 | - | - | `OnReviewOnlyToggle` / `RatedReviewContent`(`visibleReviews` 필터) |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-06 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤 - `error`는 저장되지만 렌더링 안 함) | `OnLoadMore` / `RatedReviewViewModel.loadMore` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-07 | 다시 시도 버튼 | 목록 영역이 에러로 인한 빈 상태(재시도 버튼 노출됨) | 현재 정렬 기준으로 첫 페이지부터 재조회 | - | - | `OnRetryClick` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-08 | 더보기 메뉴 - 삭제 | - | 삭제 확인 다이얼로그 표시 | - | - | `OnDeleteClick` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-09 | 삭제 확인 다이얼로그 - 삭제 | 다이얼로그 표시 중 | `deleteReview()` 성공 → 다이얼로그 닫힘 + 목록에서 해당 리뷰 제거 + `totalCount` 1 감소 + 스낵바 "리뷰가 삭제되었습니다." | `deleteReview()` 실패 | 다이얼로그는 닫히고 스낵바(API 에러 메시지) - 목록은 그대로 유지 | `RatedReviewViewModel.onDeleteConfirm` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-10 | 삭제 확인 다이얼로그 - 취소/바깥 영역 | 다이얼로그 표시 중 | 다이얼로그 닫힘(`deleteTargetReviewId = null`) | - | - | `OnDeleteDismiss` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-11 | (자동) 화면 진입 - 평가한 작품 목록 조회 | - | `getRatedAnimes(sort=ratedSort.apiValue)` 성공 → 헤더("총 n개" + 정렬 드롭다운 + 리뷰만 보기 스위치) + 리뷰 카드 목록(`AniPickReviewCard`, `showProfile=false`) 표시(로딩 중엔 스켈레톤) - `reviewOnly`는 API에 안 보내고 클라이언트 필터로만 적용 | - | - | `RatedReviewViewModel.load` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-11b | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `RatedReviewViewModel.load` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-11c | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `RatedReviewViewModel.load` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-11d | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `RatedReviewViewModel.load` |  |  |  |  |  |  |  |  |
| REVIEW-RATEDREVIEW-12 | (자동, 결과 없음) 빈 목록 | 조회 성공했지만 항목 0개(리뷰만 보기 켠 상태에서 필터링으로 0개가 된 경우 포함) | "아직 평가한 작품이 없어요." 안내 문구 표시 | - | - | `RatedReviewContent` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **좋아요는 이 화면에서 연동하지 않음** - `AniPickReviewCard`의 `onLikeClick`을 넘기지 않아 기본값(no-op)이다(요청 범위 밖).
- **정렬 API 값 미검증**: `ReviewSort.apiValue`(`latest`/`like`/`ratingDesc`/`ratingAsc`)는 실제 백엔드 스펙으로 확인된 값이 아니라 추정해 붙인 값이다. 실기기 테스트 시 정렬이 실제로 서버에서 다르게 동작하는지 반드시 확인 필요.
- **"리뷰만 보기" 필터는 목록을 다 불러온 뒤 클라이언트에서만 필터링**한다 - 첫 페이지에 리뷰(글) 있는 항목이 하나도 없으면(전부 평점만) 스위치를 켰을 때 목록이 비어 보이지만, 스크롤로 더 불러오면(무한 스크롤은 `reviewOnly`와 무관하게 원본 `state.reviews` 기준으로 계속 붙는다) 뒤 페이지에 리뷰 있는 항목이 나타날 수 있다 - 사용자 입장에선 "스크롤해야 더 보인다"는 게 직관적이지 않을 수 있음(버그는 아니고 설계상 트레이드오프, `mypage-detail.md`에서 이전된 이슈).
