# 컴포넌트 배치 컨벤션

`Screen.kt` 하나에 상태 분기, 레이아웃 계산, 무한스크롤 트리거, Modifier 트릭까지 다 들어가면 금방 몇백 줄짜리 파일이 된다. 이 문서는 **화면에서 커진 코드를 어디로 빼낼지**에 대한 기준이다. 화면 파일 구성 자체는 [`screen-structure.md`](./screen-structure.md)를 본다.

`feature/catalog/impl/studio`를 정리하면서 잡은 기준을 다른 feature 패키지에도 그대로 적용할 수 있게 정리한 것.

## Screen.kt에 있으면 안 되는 것

- 확장 함수 (`Modifier.xxx()` 포함)
- **30줄 넘는 하위 컴포넌트**
- 그 화면만을 위한 레이아웃 계산 로직

이런 게 보이면 아래 기준으로 옮긴다.

## 어디로 옮길지 판단 기준

발견한 코드가 "그 화면에서만 쓰는가" vs "재사용 가능한가"로 먼저 나누고, 재사용 가능하면 "얼마나 넓게 재사용되는가"로 위치를 정한다.

| 무엇을 옮기나 | 재사용 범위 | 옮기는 곳 | 예시 |
|---|---|---|---|
| Modifier 확장 함수 (`Modifier.xxx()`) | 앱 전체, 디자인시스템과 무관한 순수 레이아웃/그리기 로직 | `core/designsystem/extension/modifier/` | `Shadow.kt`(topEdgeShadow 등), `FullBleed.kt`(fullBleedHorizontal) |
| Context 확장 함수 | 앱 전체 | `core/designsystem/extension/context/` | `requireActivity` |
| 순수 유틸 함수 (문자열/계산 등, `@Composable` 아님) | 앱 전체 | `core/ui/util/` | `KoreanJosa.kt`(objectParticleFor), `CoverImageUrl.kt` |
| 입력값 검증 | 앱 전체 | `core/ui/validation/` (인터페이스는 `core/model/validation/`) | `EmailPatternValidator`, `PasswordPatternValidator` |
| 재사용 가능한 `@Composable` 훅/이펙트 (UI를 그리진 않고 상태·부수효과만 다룸) | 앱 전체 | `core/ui/effect/` | `LoadMoreEffect.kt` — `LazyGridState`/`LazyListState` 스크롤 끝 감지 무한스크롤 트리거. 8곳에 거의 똑같이 복붙돼 있던 `derivedStateOf`+`snapshotFlow`+`LaunchedEffect` 조합을 하나로 모음. `ObserveAsEvents.kt`, `CollapsibleHeader.kt`, `PhotoPicker.kt`도 여기 |
| 범용 `@Composable` 컴포넌트 (도메인 모델을 받는 카드/그리드 등) | 앱 전체, 여러 feature 모듈이 공유 | `core/ui/component/` | `AniPickAnimeCard`, `AniPickAnimeInfiniteGrid`, `AniPickReviewCard` |
| 디자인시스템 원시 컴포넌트 (토큰/테마만 의존, 도메인 모델 모름) | 앱 전체 | `core/designsystem/component/` | `AniPickSectionDivider`, `AniPickLoadMoreIndicator`, `AniPickRatingBox` |
| 같은 feature 모듈의 여러 화면이 같이 쓰는 컴포넌트 | 한 feature 모듈 안 (예: catalog의 recommendation + series) | `feature/{module}/impl/components/` | `CatalogBannerHeader` |
| 특정 화면 하나만 쓰는, 그 화면 전용 서브 컴포넌트 | 그 화면 하나 | `feature/{module}/impl/{screen}/components/` | `StudioAnimeSectionGrid` |

`core/ui/component/`와 `core/designsystem/component/`의 갈림길은 **도메인 모델을 아는가**다. `Anime`, `Review` 같은 모델을 파라미터로 받으면 `core/ui`, 색·타이포·치수만 알면 `core/designsystem`.

판단이 애매하면 **"지금 당장 두 번째 사용처가 있는가"**로 정한다. 없으면 일단 화면 전용(`{screen}/components/`)에 두고, 두 번째 사용처가 생기면 그때 한 단계 위(`{module}/impl/components/` 또는 `core/ui`)로 승격한다. 미리 넓은 범위로 만들어두지 않는다.

## 가시성

- `core/*`로 옮긴 것: `public`. 다른 모듈에서 그대로 import해서 쓴다.
- `feature/{module}/impl/components/`: `internal`. 같은 gradle 모듈 안의 다른 패키지에서는 보이지만 모듈 밖으로는 안 나간다.
- `feature/{module}/impl/{screen}/components/`: `internal`. 같은 이유 — 모듈 밖에서 볼 이유가 없다.
- `Screen.kt` 안의 `XxxScreen` 자체: `private`. `XxxRoot`(모듈 내 네비게이션에서 호출)만 `internal`로 노출한다.

## 네이밍

- `core/designsystem`, `core/ui`의 공개 **컴포저블 함수명**은 `AniPick` 접두사를 붙인다 (`AniPickAnimeCard`, `AniPickTitleTopAppBar`, `AniPickRatingBox`).
- **파일명은 모듈마다 다르다.** `core/designsystem/component/`는 접두사 없이 역할명으로 묶는다(`TopAppBar.kt` 안에 `AniPickTitleTopAppBar` 등 관련 컴포넌트 여러 개). `core/ui/component/`는 컴포넌트당 한 파일이라 함수명을 그대로 파일명으로 쓴다(`AniPickAnimeCard.kt`).
- feature 안의 `internal` 컴포넌트는 접두사 없이 역할 그대로 (`StudioAnimeSectionGrid`, `CatalogBannerHeader`).

## 적용 예시: `feature/catalog/impl/studio`

정리 전 `CatalogStudioScreen.kt` 하나(200줄+)에 `fullBleedHorizontal` 확장 함수, 연도별 섹션 그리드 로직(그룹핑·무한스크롤 트리거·레이아웃 계산·카드/구분선 렌더링)이 전부 들어있었다. 정리 후:

```
core/designsystem/extension/modifier/
└── FullBleed.kt                      # Modifier.fullBleedHorizontal — 앱 전체에서 재사용 가능한 순수 레이아웃 트릭

feature/catalog/impl/studio/
├── CatalogStudioAction.kt
├── CatalogStudioScreen.kt            # Root + Scaffold + 로딩/콘텐츠 분기만 (60줄대)
├── CatalogStudioState.kt
├── CatalogStudioViewModel.kt
└── components/
    └── StudioAnimeSectionGrid.kt     # 연도별 섹션 그리드 전체 — studio 화면 하나만 쓰므로 로컬 components/
```

`fullBleedHorizontal`은 studio의 divider 문제를 고치다 나온 것이지만 studio와 아무 관련 없는 순수 레이아웃 유틸이라 바로 `core/designsystem`으로, `StudioAnimeSectionGrid`는 studio 화면 전용 로직이라 로컬 `components/`로 보냈다.

## 체크리스트

새 화면을 만들거나 기존 화면을 정리할 때:

1. `Screen.kt`에 `private fun` 하위 컴포넌트가 두 개 이상, 또는 하나라도 **30줄**을 넘는가? → 분리 후보.
2. `Modifier.xxx()` 형태의 확장 함수가 있는가? → `core/designsystem/extension/modifier/`.
3. `@Composable`인데 UI를 안 그리고 상태·부수효과만 다루는가? → `core/ui/effect/`.
4. 그 컴포넌트가 도메인 모델(`Anime`, `Actor` 등)만 받고 특정 feature 로직에 의존하지 않는가? → `core/ui/component/`. 도메인 모델도 모르고 토큰만 쓰는가? → `core/designsystem/component/`.
5. 같은 모듈의 다른 화면도 이미 쓰고 있거나 곧 쓸 예정인가? → `feature/{module}/impl/components/`.
6. 위 전부 아니면 → `feature/{module}/impl/{screen}/components/`.
