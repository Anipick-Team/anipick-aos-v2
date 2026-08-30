# Search - 검색 결과 (SearchDetailScreen)

패키지: `feature/search/impl/detail`. 근거 파일: `SearchDetailAction.kt`, `SearchDetailState.kt`, `SearchDetailViewModel.kt`, `SearchDetailScreen.kt`, `detail/components/SearchTypeTabRow.kt`, `SearchActorList.kt`, `SearchStudioList.kt`, `components/SearchAnimeGrid.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

작품/인물/제작사 세 탭이 각자 자기 카테고리를 검색하지만 실패 처리(`NO_INTERNET`/`Api`/그 외)는 세 탭 다 완전히 같은 모양이라, 세부 실패 케이스(01c~e)는 작품 탭 기준으로만 적어두고 인물/제작사 탭은 "01c~e와 동일"로 축약했다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| SEARCH-SEARCHDETAIL-01 | 검색 버튼 | 검색어 입력됨 | 세 카테고리 결과를 전부 초기화한 뒤 현재 탭 기준으로 재검색(첫 페이지부터) 성공 → 결과/탭별 카운트 갱신 + 최근 검색어에 저장 | - | - | `SearchDetailViewModel.onSearch` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-01b | 검색 버튼 | 검색어 비어있음 | 아무 동작 없음(no-op) - 재조회 안 함 | 검색어 비어있음 | - (스낵바/토스트 없음 - 전역 스낵바가 화면 상단 검색창과 겹쳐서 안 씀) | `SearchDetailViewModel.onSearch` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-01c | 검색 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `SearchDetailViewModel.searchAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-01d | 검색 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `SearchDetailViewModel.searchAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-01e | 검색 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `SearchDetailViewModel.searchAnimes` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-02 | 검색어 지우기(X 아이콘) | 검색어 입력됨 | 검색어 입력란 초기화 | - | - | `SearchDetailViewModel.onAction`(`OnSearchClearClick`) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-03 | 작품/인물/제작사 탭 전환 | 전환하려는 탭이 현재 탭과 다름 | 선택한 탭 기준으로 재검색(첫 페이지부터) | - | - | `SearchDetailViewModel.onTabChanged` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-03b | 작품/인물/제작사 탭 전환 | 위와 동일 | 위와 동일 | 조회 실패(네트워크/Api/그 외) | SEARCH-SEARCHDETAIL-01c~e와 동일 | `SearchDetailViewModel.searchAnimes`/`searchActors`/`searchStudios` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-04 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (`error`엔 저장되지만 목록이 이미 안 비어있어 화면엔 안 뜸 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `SearchDetailViewModel.loadMore` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-05 | 다시 시도 버튼 | 검색 실패로 재시도 버튼이 노출된 상태 | 현재 탭 기준으로 재검색 — 초기 스켈레톤부터 다시 시작 | - | - | `SearchDetailViewModel.onAction`(`OnRetryClick`) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-06 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `SearchDetailScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-07 | 작품 카드 클릭 | 작품 탭 | 애니 상세 화면으로 이동 | - | - | `SearchDetailScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-08 | 인물 카드 클릭 | 인물 탭 | 배우 상세 화면으로 이동 | - | - | `SearchDetailScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-09 | 제작사 행 클릭 | 제작사 탭 | 스튜디오 상세 화면으로 이동 | - | - | `SearchDetailScreen` (Root) |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-10 | 화면 진입(자동) | - | 전달받은 검색어로 작품 탭 기준 검색 성공 + 최근 검색어에 저장 | - | - | `SearchDetailViewModel.init` |  |  |  |  |  |  |  |  |
| SEARCH-SEARCHDETAIL-10b | 화면 진입(자동) | - | 위와 동일 | 최초 검색 실패(네트워크/Api/그 외) | SEARCH-SEARCHDETAIL-01c~e와 동일 | `SearchDetailViewModel.init` / `searchAnimes` |  |  |  |  |  |  |  |  |
