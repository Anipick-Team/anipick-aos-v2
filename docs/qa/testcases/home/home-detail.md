# Home - 전체보기 (DetailScreen)

패키지: `feature/home/impl/detail`. 근거 파일: `HomeDetailAction.kt`, `HomeDetailState.kt`, `HomeDetailViewModel.kt`, `HomeDetailScreen.kt`, `detail/components/DetailHeader.kt`, `detail/components/ComingSoonSortHeader.kt`. `HomeDetailType`(Recommendation/Weekly/ComingSoon)에 따라 데이터 소스와 헤더만 바뀌고 나머지 레이아웃(그리드/스켈레톤/에러/재시도)은 공유한다. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고. 요일별 신작(Weekly)은 백엔드 API 미완성으로 이번 정리에서 제외 — 아래 "알려진 미완성" 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| HOME-HOMEDETAIL-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `HomeDetailScreen`(Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-02 | 애니 카드 클릭 | - | 애니 상세 화면으로 이동 | - | - | `HomeDetailScreen`(Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-03 | 요일 선택(Weekly 헤더) | - | 대상 외 — 요일별 신작 API 미완성으로 이 타입 자체에 실기기에서 진입할 방법이 없음(`home-main.md`의 "알려진 미완성" 참고) | - | - | `HomeDetailViewModel.loadWeekly` |  | 이번 정리 범위 제외 | | | | | | |
| HOME-HOMEDETAIL-04 | 정렬 기준 선택(ComingSoon 헤더, 최신순/인기순/방영 예정 순) | ComingSoon 타입으로 진입한 상태 | 선택한 기준으로 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `HomeDetailViewModel.onAction`(`OnSortSelected`) |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-04b | 정렬 기준 선택(ComingSoon 헤더) | 위와 동일 | 위와 동일 | 재조회 실패 | 목록이 이미 비어있지 않으면 화면엔 안 뜸(`error`엔 저장되지만 스켈레톤 이후 기존 목록 유지, 표시할 UI 없음) — 최초 진입 자체가 실패하는 경우는 HOME-HOMEDETAIL-09b/c/d 참고 | `HomeDetailViewModel.loadComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-05 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 + 커서 있음 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (`error`엔 저장되지만 목록이 이미 안 비어있어 화면엔 안 뜸 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `HomeDetailViewModel.loadMore` / `loadMoreRecommendation` / `loadMoreComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-06 | 다시 시도 버튼 | HOME-HOMEDETAIL-07b/c/d 또는 09b/c/d 상태(재시도 버튼 노출됨) | 현재 타입 기준으로 목록 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `HomeDetailViewModel.onAction`(`OnRetryClick`) → `retry` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-07 | 화면 진입(자동, Recommendation·취향 기반 — `basedOnAnimeId == null`) | HomeMain "오늘의 추천작 더보기"로 진입 | 취향 기반 추천 목록 조회 성공 → 표시(로딩 중엔 스켈레톤). 헤더에 닉네임 + 기준 애니 제목 반영 | - | - | `HomeDetailViewModel.loadRecommendation` / `DetailHeader`(`RecommendationSectionTitle`) |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-07b | 화면 진입(자동) | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `HomeDetailViewModel.loadRecommendation` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-07c | 화면 진입(자동) | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `HomeDetailViewModel.loadRecommendation` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-07d | 화면 진입(자동) | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `HomeDetailViewModel.loadRecommendation` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-08 | 화면 진입(자동, Recommendation·유사 작품 기반 — `basedOnAnimeId != null`) | HomeMain "최근 확인한 애니 기반 추천 더보기"로 진입 | 해당 애니 기준 추천 목록 조회 성공 → 표시. 헤더엔 기준 애니 제목만(닉네임 관찰 안 함) | 실패(네트워크/`Api`/알 수 없음) | HOME-HOMEDETAIL-07b/c/d와 동일 로직(`toDisplayMessage()` 공통 분기) | `HomeDetailViewModel.loadRecommendation` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-09 | 화면 진입(자동, ComingSoon) | HomeMain "공개 예정 더보기"로 진입 | 공개 예정 목록 조회 성공(정렬 미지정 시 최신순 취급) → 표시 | - | - | `HomeDetailViewModel.loadComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-09b | 화면 진입(자동) | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `HomeDetailViewModel.loadComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-09c | 화면 진입(자동) | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `HomeDetailViewModel.loadComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-09d | 화면 진입(자동) | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `HomeDetailViewModel.loadComingSoon` |  |  |  |  |  |  |  |  |
| HOME-HOMEDETAIL-10 | (화면 간 자동 반영 확인) 닉네임 표시 | Recommendation·취향 기반(`basedOnAnimeId == null`)으로 진입한 상태 | 설정 상세에서 닉네임 변경에 성공하면, 헤더 문구("OO 님의 취향에 맞춰...")가 재진입 없이 새 닉네임으로 갱신 | - | - | 읽는 곳: `HomeDetailViewModel.observeNickname` — 쓰는 곳/전체 구독자 목록은 `mypage/setting-detail.md`의 `MYPAGE-SETTINGDETAIL-NICKNAME-04` 참고 |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **요일별 신작(Weekly, HOME-HOMEDETAIL-03)**: `home-main.md`와 동일한 이유로 백엔드 API가 준비되지 않아 `HomeDetailViewModel.loadWeekly`가 주석 처리된 no-op다. Main의 "요일별 신작" 섹션이 항상 숨겨져 있어(`weeklyAnimes` 빈 리스트 고정) 이 타입으로 Detail에 진입할 경로 자체가 실기기에 없다. 이번 클린코딩/QA 작업 범위에서 사용자 요청으로 제외.
