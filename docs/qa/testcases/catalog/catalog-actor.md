# Catalog - 성우 상세 (CatalogActorScreen)

패키지: `feature/catalog/impl/actor`. 근거 파일: `CatalogActorAction.kt`, `CatalogActorState.kt`, `CatalogActorViewModel.kt`, `CatalogActorScreen.kt`, `components/CatalogActorComponents.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGACTOR-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-02 | 출연작 카드 클릭 | `state.works`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-03 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤 - `error`는 저장되지만 렌더링 안 함) | `OnLoadMore` / `CatalogActorViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-04 | (자동) 화면 진입 - 성우 정보/출연작 조회 | - | `getActorDetail()` 성공 → 이름/프로필/좋아요 수(`isLiked`)/출연작 그리드 표시(로딩 중엔 스켈레톤) | - | - | `CatalogActorViewModel.loadActorDetail` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-04b | (자동) 화면 진입 | - | 위와 동일 | `getActorDetail()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 `CatalogActorScreen`은 렌더링하지 않음 | `CatalogActorViewModel.loadActorDetail` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGACTOR-04b)**: `state.error`가 화면 어디에도 렌더링되지 않는다.
- **상단바 타이틀이 항상 고정 문자열 "성우"** - 실제 성우 이름(`state.name`)이 아니다.
- **`state.isLiked`가 화면에 표시되지 않음** - API 응답으로 로드는 되지만(`isLiked = page.isLiked ?: false`) 렌더링하는 곳이 없고, 좋아요를 토글하는 액션도 없다(읽기 전용 값이지만 UI에 안 보임).
