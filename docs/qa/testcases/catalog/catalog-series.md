# Catalog - 시리즈 전체보기 (CatalogSeriesScreen)

패키지: `feature/catalog/impl/series`. 근거 파일: `CatalogSeriesAction.kt`, `CatalogSeriesState.kt`, `CatalogSeriesViewModel.kt`, `CatalogSeriesScreen.kt`, `components/CatalogSeriesHeader.kt`.

애니 상세의 "시리즈(관련 작품)" 더보기에서 `animeId`+`animeTitle`(헤더 문구용, 진입 시 넘겨받고 재조회 안 함)을 받아 진입한다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGSERIES-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSERIES-02 | 시리즈 작품 카드 클릭 | `state.animes`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSERIES-03 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤) | `OnLoadMore` / `CatalogSeriesViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSERIES-04 | (자동) 화면 진입 - 시리즈 작품 목록 조회 | - | `getAnimeSeries(animeId)` 성공 → 헤더(진입 시 넘겨받은 `animeTitle` + "총 n개") + 작품 그리드 표시 | - | - | `CatalogSeriesViewModel.loadSeries` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSERIES-04b | (자동) 화면 진입 | - | 위와 동일 | `getAnimeSeries()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 렌더링 안 함 | `CatalogSeriesViewModel.loadSeries` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGSERIES-04b)**.
