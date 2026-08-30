# Catalog - "이 작품과 비슷한 작품" 전체보기 (CatalogRecommendationScreen)

패키지: `feature/catalog/impl/recommendation`. 근거 파일: `CatalogRecommendationAction.kt`, `CatalogRecommendationState.kt`, `CatalogRecommendationViewModel.kt`, `CatalogRecommendationScreen.kt`, `components/CatalogRecommendationHeader.kt`.

애니 상세의 추천 섹션 더보기에서 `basedOnAnimeId` 기준으로 조회한다(`HomeDetailType.Recommendation`과 동일한 기준 애니 개념).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGRECOMMENDATION-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGRECOMMENDATION-02 | 추천 작품 카드 클릭 | `state.animes`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGRECOMMENDATION-03 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤) | `OnLoadMore` / `CatalogRecommendationViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGRECOMMENDATION-04 | (자동) 화면 진입 - 추천 작품 목록 조회 | - | `getAnimeRecommendations(basedOnAnimeId)` 성공 → 헤더(`referenceAnimeTitle` 기준 문구) + 작품 그리드 표시 | - | - | `CatalogRecommendationViewModel.loadRecommendations` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGRECOMMENDATION-04b | (자동) 화면 진입 | - | 위와 동일 | `getAnimeRecommendations()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 렌더링 안 함 | `CatalogRecommendationViewModel.loadRecommendations` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGRECOMMENDATION-04b)**.
