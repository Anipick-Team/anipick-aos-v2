# Ranking - 랭킹 (RankingScreen)

패키지: `feature/ranking/impl`. 근거 파일: `RankingAction.kt`, `RankingState.kt`, `RankingViewModel.kt`, `RankingScreen.kt`, `components/RankingFilterHeader.kt`, `RankingItemCard.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| RANKING-RANKING-01 | 실시간 필터 칩 | 현재 선택된 타입이 실시간이 아님 | 실시간 랭킹으로 전환 - 년도/분기·장르 필터 초기화 + 첫 페이지부터 재조회 | - | - | `RankingViewModel.onAction`(`OnRankingTypeSelected`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-02 | 역대 필터 칩 | 현재 선택된 타입이 역대가 아님 | 역대 랭킹으로 전환 - 년도/분기·장르 필터 초기화 + 첫 페이지부터 재조회 | - | - | `RankingViewModel.onAction`(`OnRankingTypeSelected`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-03 | 년도/분기 필터 칩 | - | 년도/분기 선택 바텀시트 열림 | - | - | `RankingViewModel.onAction`(`OnFilterChipClick`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-04 | 장르 필터 칩 | - | 장르 선택 바텀시트 열림 | - | - | `RankingViewModel.onAction`(`OnFilterChipClick`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-05 | 필터 바텀시트 닫기(바깥 영역 탭 등) | 필터 바텀시트 열려있음 | 바텀시트만 닫힘 - 필터 선택값은 변경되지 않음 | - | - | `RankingViewModel.onAction`(`OnFilterSheetDismiss`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-06 | 필터 바텀시트 "적용"(년도/분기 탭에서 진입) | 년도/분기 바텀시트 열려있음 | 선택한 년도/분기 적용 + 랭킹 타입이 "시즌"으로 전환 + 첫 페이지부터 재조회, 바텀시트 닫힘 | - | - | `RankingViewModel.onAction`(`OnAnimeFilterConfirm`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-06b | 필터 바텀시트 "적용"(장르 탭에서 진입) | 장르 바텀시트 열려있음 | 장르만 적용(랭킹 타입은 그대로 유지) + 첫 페이지부터 재조회, 바텀시트 닫힘 | - | - | `RankingViewModel.onAction`(`OnAnimeFilterConfirm`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-07 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (`error`엔 저장되지만 목록이 이미 안 비어있어 화면엔 안 뜸 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `RankingViewModel.loadMore` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-08 | 다시 시도 버튼 | RANKING-RANKING-09b/c/d 상태(재시도 버튼 노출됨) | 현재 랭킹 타입 기준으로 목록 재조회 — 초기 스켈레톤부터 다시 시작 | - | - | `RankingViewModel.onAction`(`OnRetryClick`) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-09 | 검색 아이콘 | - | 검색 화면으로 이동 | - | - | `RankingScreen` (Root) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-10 | 랭킹 아이템 카드 클릭 | - | 애니 상세 화면으로 이동 | - | - | `RankingScreen` (Root) |  |  |  |  |  |  |  |  |
| RANKING-RANKING-11 | 화면 진입(자동) | - | 현재 랭킹 타입 기준 목록 조회 성공 → 목록 표시(로딩 중엔 스켈레톤) | - | - | `RankingViewModel.init` / `loadRankings` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-11b | 화면 진입(자동) | - | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `RankingViewModel.loadRankings` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-11c | 화면 진입(자동) | - | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `RankingViewModel.loadRankings` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-11d | 화면 진입(자동) | - | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `RankingViewModel.loadRankings` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-12 | 화면 진입(자동) | - | 필터 메타데이터(선택 가능한 년도/분기/장르 목록) 조회 성공 → 필터 바텀시트 선택지에 반영 | - | - | `RankingViewModel.fetchMetadata` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-12b | 화면 진입(자동) | - | 위와 동일 | 메타데이터 조회 실패 — 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." (지속 UI 없음) | `RankingViewModel.handleMetadataFailure` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-12c | 화면 진입(자동) | - | 위와 동일 | 메타데이터 조회 실패 — 그 외(서버 통신 문제) | 필터 바텀시트를 열면 시트 콘텐츠 영역에 "필터 정보를 불러오지 못했습니다." + "다시 시도" 버튼 표시(`isMetadataError`, `AniPickAnimeFilterBottomSheet`) | `RankingViewModel.handleMetadataFailure` / `RankingScreen` |  |  |  |  |  |  |  |  |
| RANKING-RANKING-12d | 필터 바텀시트 내 "다시 시도" 버튼 | RANKING-RANKING-12c 상태(필터 바텀시트가 재시도 UI를 보여줌) | 메타데이터 재조회 — 성공 시 바텀시트에 정상 필터 콘텐츠 표시(RANKING-RANKING-12와 동일 분기 재적용) | - | - | `RankingViewModel.onAction`(`OnMetadataRetryClick`) |  |  |  |  |  |  |  |  |
