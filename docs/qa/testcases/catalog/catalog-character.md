# Catalog - 등장인물/성우 목록 (CatalogCharacterScreen)

패키지: `feature/catalog/impl/character`. 근거 파일: `CatalogCharacterAction.kt`, `CatalogCharacterState.kt`, `CatalogCharacterViewModel.kt`, `CatalogCharacterScreen.kt`.

애니 상세의 "등장인물/성우 목록 전체보기"에서 진입한다. 목록 항목은 클릭 액션이 없다(표시 전용).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGCHARACTER-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGCHARACTER-02 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤) | `OnLoadMore` / `CatalogCharacterViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGCHARACTER-03 | (자동) 화면 진입 - 등장인물/성우 목록 조회 | - | `getAnimeCharacters(animeId)` 성공 → 목록 표시(로딩 중엔 스켈레톤) | - | - | `CatalogCharacterViewModel.loadAnimeCharacters` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGCHARACTER-03b | (자동) 화면 진입 | - | 위와 동일 | `getAnimeCharacters()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 렌더링 안 함 | `CatalogCharacterViewModel.loadAnimeCharacters` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGCHARACTER-03b)**.
- **항목 클릭 시 아무 동작 없음** - 캐릭터/성우 상세로 이동하는 기능 자체가 없다(`Action`에 클릭 이벤트가 정의돼 있지 않음). 성우 카드를 눌러 `CatalogActorScreen`으로 이동하는 게 자연스러운 동작으로 보이는데 연결이 안 되어 있음.
