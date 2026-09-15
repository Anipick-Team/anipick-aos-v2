# Catalog - 성우 상세 (CatalogActorScreen)

패키지: `feature/catalog/impl/actor`. 근거 파일: `CatalogActorAction.kt`, `CatalogActorState.kt`, `CatalogActorViewModel.kt`, `CatalogActorScreen.kt`, `components/CatalogActorComponents.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| CATALOG-CATALOGACTOR-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `OnBackClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-02 | 출연작 카드 클릭 | `state.works`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-03 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | - (조용히 멈춤 - `error`는 저장되지만 렌더링 안 함) | `OnLoadMore` / `CatalogActorViewModel.loadMore` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-04 | (자동) 화면 진입 - 성우 정보/출연작 조회 | - | `getActorDetail()` 성공 → 이름/프로필/좋아요 수(`isLiked`)/출연작 그리드 표시(로딩 중엔 스켈레톤) | - | - | `CatalogActorViewModel.loadActorDetail` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-04b | (자동) 화면 진입 | - | 위와 동일 | `getActorDetail()` 실패(네트워크/Api/알 수 없음 무관) | UI 반응 없음 - `state.error`만 갱신되고 `CatalogActorScreen`은 렌더링하지 않음 | `CatalogActorViewModel.loadActorDetail` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-05 | 찜(좋아요) 아이콘 | - | 낙관적 토글 - `state.isLiked` 즉시 반전 후 `likeActor`/`unlikeActor` 호출 | `likeActor`/`unlikeActor` 실패 | 스낵바(API 에러 메시지) + `isLiked` 원상복구 | `CatalogActorViewModel.toggleLike` |  |  |  |  |  |  |  |  |
| CATALOG-CATALOGACTOR-06 | 찜(좋아요) 아이콘 (화면 간 자동 반영 확인) | CATALOG-CATALOGACTOR-05와 동일하게 찜 토글 성공 | `likeActor`/`unlikeActor` 성공 시 `UserRepository.refreshMyPage()`로 `myPageProfile`이 갱신되어, 재진입/새로고침 없이 마이페이지 메인/디테일(LikedPersons)의 "좋아요한 인물" 목록·카운트가 최신 상태로 보인다 | - | - | 쓰는 곳: `ActorRepositoryImpl.likeActor`/`unlikeActor` / 읽는 곳: [`mypage/mypage-main.md`](../mypage/mypage-main.md), [`mypage/mypage-detail.md`](../mypage/mypage-detail.md) |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **조회 실패 시 사용자 피드백 없음 (CATALOG-CATALOGACTOR-04b)**: `state.error`가 화면 어디에도 렌더링되지 않는다.
- **상단바 타이틀이 항상 고정 문자열 "성우"** - 실제 성우 이름(`state.name`)이 아니다.
