# Search - 검색 메인 (SearchMainScreen)

패키지: `feature/search/impl/main`. 근거 파일: `SearchMainAction.kt`, `SearchMainState.kt`, `SearchMainEvent.kt`, `SearchMainViewModel.kt`, `SearchMainScreen.kt`, `main/components/RecentSearchesSection.kt`, `components/SearchAnimeGrid.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| SEARCH-SEARCHMAIN-01 | 검색 버튼(돋보기 아이콘) | 검색어 입력됨 | 검색 상세 화면으로 이동(입력한 검색어 그대로) | - | - | `SearchMainViewModel.search` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-01b | 검색 버튼 | 검색어 비어있음 | 아무 동작 없음(no-op) - 화면 안 넘어감 | 검색어 비어있음 | - (스낵바/토스트 없음 - 전역 스낵바가 화면 상단 검색창과 겹쳐서 안 씀) | `SearchMainViewModel.search` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-02 | 검색어 지우기(X 아이콘) | 검색어 입력됨 | 검색어 입력란 초기화 | - | - | `SearchMainViewModel.onAction`(`OnSearchClearClick`) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-03 | 최근 검색어 칩 클릭 | 최근 검색어 1개 이상 | 그 검색어로 검색 상세 화면으로 바로 이동(빈 검색어 검증 없이) | - | - | `SearchMainScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-04 | 최근 검색어 개별 삭제(칩의 X) | 최근 검색어 1개 이상 | 그 검색어만 목록에서 제거 — `recentSearches`가 Flow 구독이라 재진입 없이 바로 반영 | - | - | `SearchMainViewModel.removeRecentSearch` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-05 | 전체 삭제 | 최근 검색어 1개 이상 | 최근 검색어 목록 전체 제거 | - | - | `SearchMainViewModel.clearRecentSearches` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-06 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `SearchMainScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-07 | 인기 작품 카드 클릭 | - | 애니 상세 화면으로 이동 | - | - | `SearchMainScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-08 | 화면 진입(자동) | - | 인기 작품 목록 조회 성공 → 목록 표시(로딩 중엔 스켈레톤) | - | - | `SearchMainViewModel.loadPopularAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-08b | 화면 진입(자동) | - | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `SearchMainViewModel.loadPopularAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-08c | 화면 진입(자동) | - | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `SearchMainViewModel.loadPopularAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-08d | 화면 진입(자동) | - | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `SearchMainViewModel.loadPopularAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-09 | 다시 시도 버튼 | SEARCH-SEARCHMAIN-08b/c/d 상태(재시도 버튼 노출됨) | 인기 작품 목록 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `SearchMainViewModel.onAction`(`OnRetryClick`) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHMAIN-10 | 화면 진입(자동) | - | 최근 검색어 목록 구독 - 1개 이상이면 "최근 검색어" 섹션 표시, 없으면 섹션 자체가 안 보임 | - | - | `SearchMainViewModel.observeRecentSearches` |  |  |  |  |  |  |  |  |
