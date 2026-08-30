# Home - 메인 (MainScreen)

패키지: `feature/home/impl/main`. 근거 파일: `HomeMainAction.kt`, `HomeMainState.kt`, `HomeMainEvent.kt`, `HomeMainViewModel.kt`, `HomeMainScreen.kt`, `main/components/HomeMainSections.kt`, `main/components/HomeMainComponents.kt`, `components/DayOfWeekSelector.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| HOME-HOMEMAIN-01 | 검색 아이콘 | - | 검색 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-02 | 애니 카드 클릭(모든 섹션 공통) | - | 애니 상세 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-03 | 요일 선택(월~일) | 요일별 신작 섹션이 보이는 상태 | 선택한 요일 하이라이트로 변경 | - | - | `HomeMainViewModel.selectDay` |  | 백엔드 API 미완성으로 실제 데이터 조회는 없음(아래 근거 참고) — 요일 선택 UI 자체가 하이라이트만 바뀌고 목록은 그대로 빈 상태 | | | | | | |
| HOME-HOMEMAIN-04 | 실시간 인기 더보기 | 실시간 인기 섹션이 보이는 상태(`trendingAnimes` 비어있지 않음) | 랭킹 탭으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-05 | 오늘의 추천작 더보기 | - | HomeDetail(Recommendation) 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-06 | 요일별 신작 더보기 | 요일별 신작 섹션이 보이는 상태(`weeklyAnimes` 비어있지 않음) | HomeDetail(Weekly) 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  | 현재는 사전조건 자체가 항상 거짓(아래 근거 참고)이라 실기기에서 이 버튼을 노출/클릭할 방법이 없음 | | | | | | |
| HOME-HOMEMAIN-07 | 최근 리뷰 더보기 | 최근 리뷰 섹션이 보이는 상태(`recentReviews` 비어있지 않음) | 최근 리뷰 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-08 | 방영예정 더보기 | 방영예정 섹션이 보이는 상태(`upcomingSeason.animes` 비어있지 않음) | 탐색 탭으로 이동 — `upcomingSeason.seasonYear`/`.season`을 탐색 화면 진입 필터 초기값으로 전달(`ExploreEntryFilter`) | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-09 | 최근 확인한 애니 기반 추천 더보기 | `recentAnimeRecommendationAnimeId`가 null이 아님(최근 확인한 애니가 있음) | HomeDetail(Recommendation(basedOnAnimeId)) 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-10 | 공개 예정 더보기 | 공개 예정 섹션이 보이는 상태(`comingSoonAnimes` 비어있지 않음) | HomeDetail(ComingSoon) 화면으로 이동 | - | - | `HomeMainScreen` (Root) |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-11 | 다시 시도 버튼 | HOME-HOMEMAIN-12b 상태(연결 에러 화면 노출됨) | 디바운스 무시하고 6개 섹션 강제 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `HomeMainViewModel.onAction`(`OnRetryClick`) → `refresh(force = true)` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-12 | 화면 진입(자동) | - | 6개 섹션(실시간 인기/추천/요일별 신작/최근 리뷰/방영예정/공개 예정) 동시 조회 성공 → 섹션별로 표시(로딩 중엔 스켈레톤). 추천/최근확인추천 섹션은 데이터가 비어있으면 안내 이미지로 대체 표시, 그 외 섹션은 비어있으면 섹션 자체가 숨겨짐 | - | - | `HomeMainViewModel.refresh` / `HomeMainSections.homeMainSections` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-12b | 화면 진입(자동) | - | 위와 동일 | 6개 섹션 전부 실패(=네트워크 연결 자체 문제로 판단) | "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`), 화면 전체가 이 상태로 전환됨 | `HomeMainViewModel.refresh` / `HomeMainState.isConnectionError` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-12c | 화면 진입(자동) | - | 위와 동일 | 6개 중 일부만 실패 | 없음 — 실패한 섹션만 조용히 안 보이고 나머지 섹션은 정상 표시(별도 안내 UI 없음) | `HomeMainViewModel.refresh` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-13 | (관찰 효과) 로그아웃 상태에서 로그인 성공(재로그인) | HomeMain이 떠 있는 상태에서 로그인 완료 | 디바운스 무시하고 6개 섹션 강제 재조회(HOME-HOMEMAIN-12/12b/12c와 동일 분기 재적용) | - | - | `HomeMainViewModel.observeLoginState` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-14 | (관찰 효과) 다른 화면에서 애니 상세 열람(최근 확인 애니 갱신) | HomeMain이 떠 있는 상태에서 다른 화면으로 이동해 애니 상세를 봄 | "최근 확인한 애니 기반 추천" 섹션이 재진입 없이 새 애니 기준으로 자동 갱신(실패 시 조용히 이전 상태 유지, 별도 에러 UI 없음) | 추천 조회 실패 | 없음 — 섹션이 갱신되지 않고 이전 상태 유지 | `HomeMainViewModel.observeRecentAnimeRecommendations` / `animeRepository.recentAnimeId` |  |  |  |  |  |  |  |  |
| HOME-HOMEMAIN-15 | (화면 간 자동 반영 확인) 닉네임 표시 | - | 설정 상세에서 닉네임 변경에 성공하면, HomeMain의 "오늘의 추천작" 섹션 제목("OO 님의 취향에 맞춰...")이 재진입 없이 새 닉네임으로 갱신 | - | - | 읽는 곳: `HomeMainViewModel.observeNickname` — 쓰는 곳/전체 구독자 목록은 `mypage/setting-detail.md`의 `MYPAGE-SETTINGDETAIL-NICKNAME-04` 참고 |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **요일별 신작 (HOME-HOMEMAIN-03/06)**: 백엔드 API가 아직 준비되지 않아 `HomeMainViewModel.loadWeeklyAnimes`의 실제 네트워크 호출이 주석 처리돼 있다(`// TODO: 요일별 신작 API 미완성...`). `weeklyAnimes`가 항상 빈 리스트로 고정되므로 "요일별 신작" 섹션 자체가 화면에 노출되지 않고, 그 안의 `DayOfWeekSelector`/더보기 버튼도 실기기에서 트리거할 방법이 없다. 백엔드 준비되면 주석만 풀면 되도록 구조는 이미 만들어져 있음.
