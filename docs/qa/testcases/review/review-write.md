# Review - 리뷰 작성/수정 (ReviewWriteScreen)

패키지: `feature/review/impl/write`. 근거 파일: `ReviewWriteAction.kt`, `ReviewWriteState.kt`, `ReviewWriteViewModel.kt`, `ReviewWriteScreen.kt`. 애니 상세 리뷰 탭의 "상세 리뷰 작성하기" 버튼에서 `animeId` 기준으로 진입한다 - 기존 리뷰가 있으면(`getMyReview`가 `reviewId` 있는 값을 반환) 수정 모드, 없으면 작성 모드로 자동 분기(`isEditMode`).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| REVIEW-REVIEWWRITE-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-02 | 별점 탭/드래그(`AniPickRatingBox`) | - | 별점 값 갱신(`state.rating`), 0.5 단위 - 별점이 0보다 크면 등록 버튼 활성화 | - | - | `OnRatingChanged` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-03 | "스포일러" 스위치 | - | `isSpoiler` 상태 토글 | - | - | `OnSpoilerToggle` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-04 | "커뮤니티 가이드라인 전체보기" 버튼 | - | 미구현 - 아무 동작 없음 | - | - | `OnGuidelineClick`(Root의 TODO 주석) |  | 실제 가이드라인 URL/화면이 아직 없어 연동 보류 | | | | | | |
| REVIEW-REVIEWWRITE-05 | "등록"/"수정" 버튼(상단바, `AniPickTopBarSubmitAction`) | `rating > 0`(`isSubmitEnabled`) | `updateReview(animeId, content, rating, isSpoiler)` 성공 → 스낵바("리뷰가 등록되었습니다." 작성 모드 / "리뷰가 수정되었습니다." 수정 모드) → `SubmitSuccess` 이벤트 → 이전 화면으로 이동(뒤로가기와 동일 목적지) | - | - | `OnSubmitClick` / `ReviewWriteViewModel.onSubmitClick` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-05b | "등록"/"수정" 버튼 | `rating == 0`(아직 별점 안 매김) | 버튼 비활성화(회색) - 클릭 불가 | - | - | `AniPickTopBarSubmitAction(enabled = state.isSubmitEnabled)` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-05c | "등록"/"수정" 버튼 | `isSubmitEnabled == true` | 위와 동일 | `updateReview()` 실패(네트워크/Api/알 수 없음 무관) | 스낵바(`toDisplayMessage()`) | `ReviewWriteViewModel.onSubmitClick` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-06 | 리뷰 내용 입력란에 텍스트 입력 | - | 텍스트 반영 + `0/200`~`200/200` 글자 수 카운터 갱신, 200자 초과 입력 불가(`maxLength`, 라벨 없는 단순 텍스트 필드) | - | - | `ReviewWriteScreen`(`state.contentState`, `REVIEW_CONTENT_MAX_LENGTH=200`) |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-07 | (자동) 화면 진입 - 애니 정보 조회 | - | `getAnimeDetailInfo(animeId)` 성공 → `state.animeTitle`/`animeCoverImageUrl` 저장 | - | - | `ReviewWriteViewModel.loadAnimeInfo` |  | 이 값들을 실제로 화면에 렌더링하는 곳이 현재 없음(아래 "알려진 미완성" 참고) | | | | | | |
| REVIEW-REVIEWWRITE-07b | (자동) 화면 진입 | - | 위와 동일 | `getAnimeDetailInfo()` 실패 | 스낵바(`toDisplayMessage()`) | `ReviewWriteViewModel.loadAnimeInfo` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-08 | (자동) 화면 진입 - 기존 리뷰 조회 | - | `getMyReview(animeId)` 성공 → 기존 별점/내용/스포일러 여부로 폼 초기값 채움(수정 모드면 상단바 타이틀 "리뷰 수정"/버튼 "수정", 아니면 "리뷰 작성"/"등록") | - | - | `ReviewWriteViewModel.loadCurrentReview` |  |  |  |  |  |  |  |  |
| REVIEW-REVIEWWRITE-08b | (자동) 화면 진입 | - | 위와 동일 | `getMyReview()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 `ReviewWriteScreen`은 렌더링하지 않음(로딩 스피너/스켈레톤도 없음) | `ReviewWriteViewModel.loadCurrentReview` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **애니 정보(제목/커버)를 불러오지만 화면에 표시하지 않음 (REVIEW-REVIEWWRITE-07)**: `ReviewWriteState.animeTitle`/`animeCoverImageUrl`이 `loadAnimeInfo()`로 채워지긴 하지만, 현재 `ReviewWriteScreen`의 `RatingSection`(→ 공용 `AniPickRatingBox`로 교체됨)은 이 값을 렌더링하지 않는다. 상단바 타이틀도 "리뷰 작성"/"리뷰 수정" 고정 문구라 화면 어디에도 "어떤 애니에 대한 리뷰인지"가 안 보인다. 의도된 것인지 확인 필요(의도된 게 아니면 헤더에 애니 정보 표시를 다시 추가하거나, 안 쓸 거면 `loadAnimeInfo()` 자체를 정리하는 게 나음).
- **`getMyReview()` 실패 시 사용자 피드백 없음 (REVIEW-REVIEWWRITE-08b)**.
- **"커뮤니티 가이드라인 전체보기" 목적지 없음 (REVIEW-REVIEWWRITE-04)**: 실제 URL/화면이 정해지면 연동 필요(Setting의 `Intent.ACTION_VIEW` 패턴 재사용 가능).
- **주의사항 안내 텍스트는 고정 문구(임시 작성)** - 실제 정책 문구로 교체 필요할 수 있음.
