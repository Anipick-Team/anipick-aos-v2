# Catalog - 제작사 상세 (CatalogStudioScreen)

패키지: `feature/catalog/impl/studio`. 근거 파일: `CatalogStudioAction.kt`, `CatalogStudioState.kt`, `CatalogStudioViewModel.kt`, `CatalogStudioScreen.kt`, `components/StudioAnimeSectionGrid.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGSTUDIO-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSTUDIO-02 | 애니 카드 클릭 | `state.animes`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSTUDIO-03 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤) | `OnLoadMore` / `CatalogStudioViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSTUDIO-04 | (자동) 화면 진입 - 제작사 정보/제작 애니 목록 조회 | - | `getStudioAnimes(studioId)` 성공 → 제작사명 + 애니 그리드 표시(로딩 중엔 스켈레톤) | - | - | `CatalogStudioViewModel.loadStudioAnimes` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGSTUDIO-04b | (자동) 화면 진입 | - | 위와 동일 | `getStudioAnimes()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 렌더링 안 함 | `CatalogStudioViewModel.loadStudioAnimes` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGSTUDIO-04b)**.
