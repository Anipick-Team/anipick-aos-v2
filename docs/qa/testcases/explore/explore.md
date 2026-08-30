# Explore - 탐색 (ExploreScreen)

패키지: `feature/explore/impl`. 근거 파일: `ExploreAction.kt`, `ExploreState.kt`, `ExploreViewModel.kt`, `ExploreScreen.kt`, `components/ExploreFilterHeader.kt`, `components/ExploreSortHeader.kt`, `components/ExploreTabContent.kt`, `components/ExploreCommunityBoardItem.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

작품 탐색(ANIME)/커뮤니티(COMMUNITY) 두 탭을 한 화면(`ExploreScreen`)에서 다루므로, 탭 공통이 아닌 액션은 이벤트/트리거 칸에 해당 탭을 명시했다. 두 탭의 목록은 진입 시점에 함께 미리 조회해두므로, 탭 전환 자체는 재조회를 일으키지 않는다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| EXPLORE-EXPLORE-01 | 상단 탭 전환(작품 탐색 ↔ 커뮤니티) | 현재 선택된 탭이 클릭한 탭이 아님 | 해당 탭 콘텐츠로 전환(필터/조회 상태는 유지, 재조회 없음 — 두 탭 모두 진입 시 이미 조회해둠) | - | - | `ExploreViewModel.onAction`(`OnTabSelected`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-02 | 정렬 드롭다운(작품 탐색 탭: 인기순/평점순) | 작품 탐색 탭 | 선택한 정렬 기준으로 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnSortSelected`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-02b | 정렬 드롭다운(커뮤니티 탭: 인기순/최신순) | 커뮤니티 탭 | 선택한 정렬 기준으로 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnSortSelected`) |  |  | 애니 탭과 다른 옵션 세트(`ExploreSortHeader`) — API에 보낼 값도 애니 탭과 달리 "popular"/"latest" | | | | | | |
| EXPLORE-EXPLORE-03 | 년도/분기·장르·타입 필터 칩 | 작품 탐색 탭 | 해당 탭이 선택된 필터 바텀시트 열림 | - | - | `ExploreViewModel.onAction`(`OnFilterChipClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-04 | 필터 바텀시트 닫기(바깥 영역 탭 등) | 필터 바텀시트 열려있음 | 바텀시트만 닫힘 - 필터 선택값은 변경되지 않음 | - | - | `ExploreViewModel.onAction`(`OnFilterSheetDismiss`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-05 | 필터 바텀시트 "적용" | 필터 바텀시트 열려있음 | 년도/분기/타입은 그대로 교체, 장르는 다중 선택 + "모든 조건 일치" 토글(AND/OR) 결과로 전체 교체 + 첫 페이지부터 재조회, 바텀시트 닫힘 | - | - | `ExploreViewModel.onAction`(`OnFilterConfirm`) |  |  | 장르는 `AniPickAnimeFilterBottomSheet`의 `allowMultipleGenres` 모드 — 시트 안에서 다중 선택한 결과 그대로 대체하며, 이전처럼 한 번에 하나씩 누적하지 않는다 | | | | | | |
| EXPLORE-EXPLORE-06 | 년도 필터 칩 해제(x) | 작품 탐색 탭, 년도 필터 선택됨 | 년도 + 분기 함께 초기화(분기는 년도 종속) + 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnYearFilterRemove`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-07 | 분기 필터 칩 해제(x) | 작품 탐색 탭, 분기 필터 선택됨 | 분기만 초기화(년도는 유지) + 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnSeasonFilterRemove`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-08 | 장르 필터 칩 해제(x) | 작품 탐색 탭, 장르 1개 이상 선택됨 | 해당 장르만 목록에서 제거(다른 선택된 장르는 유지) + 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnGenreFilterRemove`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-09 | 타입 필터 칩 해제(x) | 작품 탐색 탭, 타입 필터 선택됨 | 타입 초기화 + 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnTypeFilterRemove`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-10 | 목록 스크롤 중 하단 도달(작품 탐색 탭, 무한 스크롤) | 작품 탐색 탭, 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (`error`엔 저장되지만 목록이 이미 안 비어있어 화면엔 안 뜸 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `ExploreViewModel.loadMore` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-10b | 목록 스크롤 중 하단 도달(커뮤니티 탭, 무한 스크롤) | 커뮤니티 탭, 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (목록이 이미 안 비어있어 화면엔 안 뜸 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `ExploreViewModel.loadMoreCommunity` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-11 | 다시 시도 버튼(작품 탐색 탭 목록 영역) | EXPLORE-EXPLORE-18b/c/d 상태(재시도 버튼 노출됨) | 현재 필터/정렬 기준으로 목록 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `ExploreViewModel.onAction`(`OnRetryClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-11b | 다시 시도 버튼(커뮤니티 탭 목록 영역) | EXPLORE-EXPLORE-21b/c/d 상태(재시도 버튼 노출됨) | 현재 정렬/검색어 기준으로 목록 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `ExploreViewModel.onAction`(`OnCommunityRetryClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-12 | 필터 바텀시트 내 "다시 시도" 버튼 | EXPLORE-EXPLORE-19c 상태(필터 바텀시트가 재시도 UI를 보여줌) | 메타데이터 재조회 — 성공 시 바텀시트에 정상 필터 콘텐츠 표시(EXPLORE-EXPLORE-19와 동일 분기 재적용) | - | - | `ExploreViewModel.onAction`(`OnMetadataRetryClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-13 | 검색 아이콘 | - | 검색 화면으로 이동 | - | - | `ExploreScreen` (Root) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-14 | 작품 카드 클릭 | 작품 탐색 탭 | 애니 상세 화면으로 이동 | - | - | `ExploreScreen` (Root) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-15 | 커뮤니티 검색(검색 아이콘) | 커뮤니티 탭 | 입력된 검색어로 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnCommunitySearchClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-16 | 커뮤니티 검색어 지우기(x) | 커뮤니티 탭, 검색어 입력됨 | 입력 필드 텍스트 초기화 + 검색어 없이 첫 페이지부터 재조회 | - | - | `ExploreViewModel.onAction`(`OnCommunitySearchClearClick`) |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-17 | 커뮤니티 목록 아이템 클릭 | 커뮤니티 탭 | 해당 작품의 커뮤니티(Board 데이터와 함께)로 이동 | - | - | `ExploreScreen` (Root) → `CommunityNavKey.Main` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-18 | 화면 진입(자동, 다른 화면에서 넘어온 필터 없이 직접 진입) | - | 현재 필터/정렬 기준(기본값) 작품 목록 조회 성공 → 목록 표시(로딩 중엔 스켈레톤) | - | - | `ExploreViewModel.init` / `loadAnimes` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-18b | 화면 진입(자동) | - | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `ExploreViewModel.loadAnimes` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-18c | 화면 진입(자동) | - | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `ExploreViewModel.loadAnimes` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-18d | 화면 진입(자동) | - | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `ExploreViewModel.loadAnimes` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-19 | 화면 진입(자동, 필터 없이 진입) | - | 필터 메타데이터(선택 가능한 년도/분기/장르/타입 목록) 조회 성공 → 필터 바텀시트 선택지에 반영 | - | - | `ExploreViewModel.fetchMetadata` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-19b | 화면 진입(자동, 필터 없이 진입) | - | 위와 동일 | 메타데이터 조회 실패 — 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." (지속 UI 없음) | `ExploreViewModel.handleMetadataFailure` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-19c | 화면 진입(자동, 필터 없이 진입) | - | 위와 동일 | 메타데이터 조회 실패 — 그 외(서버 통신 문제) | 필터 바텀시트를 열면 시트 콘텐츠 영역에 "필터 정보를 불러오지 못했습니다." + "다시 시도" 버튼 표시(`isMetadataError`, `AniPickAnimeFilterBottomSheet`) | `ExploreViewModel.handleMetadataFailure` / `ExploreScreen` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-20 | 화면 진입(자동, 다른 화면에서 년도/분기 필터를 들고 진입 — 예: HomeMain "방영예정 더보기") | 진입 시 `ExploreEntryFilter`에 년도/분기 값이 설정돼 있음 | 메타데이터 조회 성공 후 그 분기 id에 해당하는 `Season`을 찾아 필터에 자동 반영 + 그 필터로 목록 조회 | - | - | `ExploreViewModel.fetchMetadataAndApplyInitialSeason` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-20b | 화면 진입(자동, 필터 들고 진입) | 위와 동일 | 위와 동일 흐름, 단 메타데이터 조회 자체가 실패하면 EXPLORE-EXPLORE-19b/c와 동일 실패 UI가 뜸 | 메타데이터 조회 실패 | EXPLORE-EXPLORE-19b/c와 동일 | `ExploreViewModel.fetchMetadataAndApplyInitialSeason` |  | 분기 매칭에 실패했더라도(메타데이터 조회 실패로 `Season`을 못 찾아도) 목록 조회는 그대로 진행됨 — 이 경우 분기 필터 없이(년도만 반영된 채) 조회된다 | | | | | | |
| EXPLORE-EXPLORE-21 | 화면 진입(자동) | - | 커뮤니티 게시판 목록(기본 정렬 인기순) 조회 성공 → 목록 표시(로딩 중엔 스켈레톤) — 작품 목록 조회와 별개로 진입 시점에 함께 시작 | - | - | `ExploreViewModel.init` / `loadCommunityBoards` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-21b | 화면 진입(자동) | - | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `ExploreViewModel.loadCommunityBoards` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-21c | 화면 진입(자동) | - | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `ExploreViewModel.loadCommunityBoards` |  |  |  |  |  |  |  |  |
| EXPLORE-EXPLORE-21d | 화면 진입(자동) | - | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `ExploreViewModel.loadCommunityBoards` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **메타데이터 조회 실패 처리 (EXPLORE-EXPLORE-19b/c/20b)**: 기존에는 `fetchMetadata`/`fetchMetadataAndApplyInitialSeason`의 실패가 `Timber.e` 로그만 남기고 화면엔 아무 표시도 없었다(사용자는 필터를 열어도 왜 년도/장르 목록이 비어있는지 알 수 없었음). Ranking/PreferenceSetup과 동일한 패턴(`isMetadataError` + `AniPickAnimeFilterBottomSheet` 콘텐츠 영역 내 재시도 UI)으로 맞췄다.
- **정렬 값 어휘 불일치 (EXPLORE-EXPLORE-02b)**: 작품 탐색 탭은 `ExploreSort.apiValue`("popularity"/"rating"/"latest")를 그대로 API에 보내지만, 커뮤니티 게시판 목록 API(`CommunityExploreBoardsRequest.sort`)는 "popular"/"latest"를 쓴다 — 같은 `ExploreSort` enum을 재사용하되 `loadCommunityBoards`에서 별도로 문자열을 매핑한다. 새 정렬 옵션을 추가할 때 이 매핑을 놓치지 않도록 주의.
