# 화면 구성 컨벤션

새 화면을 하나 만들 때 **어떤 파일을 만들고, 각 파일에 뭐가 들어가는지**에 대한 기준. 화면 안에서 커진 컴포넌트를 어디로 빼낼지는 [`component-placement.md`](./component-placement.md)를 본다.

## 모듈 구조

feature는 `api` / `impl` 두 모듈로 쪼갠다.

```
feature/{module}/api/                       # 다른 feature가 import해도 되는 것만
└── {Module}NavKey.kt                       # sealed interface XxxNavKey : NavKey (@Serializable)

feature/{module}/impl/                      # 화면 구현. 다른 feature는 여기에 의존하지 않는다
├── di/{Module}Module.kt                    # Koin viewModel { } 등록
├── navigation/{Module}Navigation.kt        # EntryProviderScope 확장 + CONTENT_KEY 상수
├── components/                             # 이 모듈의 여러 화면이 공유하는 컴포넌트 (internal)
└── {screen}/                               # 화면 하나 = 디렉터리 하나
```

**다른 feature로 이동해야 하면 상대 모듈의 `api`(NavKey)에만 의존한다.** `impl`끼리는 절대 참조하지 않는다 — 목적지 화면이 아니라 목적지 *키*만 알면 되기 때문이다.

화면에 넘길 파라미터는 NavKey에 담는다. 그 화면 API 응답으로는 못 얻고 진입 시점에만 알 수 있는 값이면(예: 헤더에 쓸 제목) 그것도 NavKey에 넣고, 왜 넘기는지는 한 줄 주석으로 남긴다(`CatalogNavKey.Series.animeTitle` 참고).

## 화면 디렉터리 파일 구성

```
feature/{module}/impl/{screen}/
├── {Screen}Action.kt        # 필수
├── {Screen}State.kt         # 필수
├── {Screen}ViewModel.kt     # 필수
├── {Screen}Screen.kt        # 필수 — Root + Screen + Preview
├── {Screen}Event.kt         # 조건부 (아래 "Event를 만들 때" 참고)
└── components/              # 조건부 — 이 화면만 쓰는 서브 컴포넌트 (internal)
```

**빈 파일을 미리 만들지 않는다.** 특히 `sealed interface XxxEvent { }` 같은 빈 껍데기는 두지 않는다 — 다음 사람이 "이 화면은 왜 Event가 비어있지?"를 고민하게 되고, 규칙으로 오해한다. 필요해지는 시점에 만든다.

## 네비게이션: Root 콜백 vs Event

두 방법이 다 있고, **누가 목적지를 정하는가**로 고른다.

| 상황 | 방법 |
|---|---|
| 사용자가 누르면 곧바로 이동 (뒤로가기, 상세 진입, 외부 인텐트) | **Root 콜백.** ViewModel을 안 거친다. Event 만들지 않는다 |
| ViewModel이 비동기 결과를 보고 목적지를 정함 (로그인 성공 → `reviewCompletedYn`에 따라 홈/취향입력, 비밀번호 재설정 완료 → 로그인) | **Event 채널** |
| 일회성 스낵바 | **둘 다 아님.** `GlobalSnackbarManager`가 전역 처리한다 — Event로 쏘지 않는다 |

### Event를 만들 때

`Channel` + `receiveAsFlow`로 노출하고, Root에서 `ObserveAsEvents`(`core/ui/effect/`)로 받는다.

```kotlin
// ViewModel
private val _events = Channel<LoginEvent>()
val events = _events.receiveAsFlow()

// Root
ObserveAsEvents(viewModel.events) { event ->
    when (event) {
        LoginEvent.NavigateToHome -> onNavigateToHome()
        LoginEvent.NavigateToPreferenceSetup -> onNavigateToPreferenceSetup()
    }
}
```

`StateFlow`에 네비게이션 신호를 담지 않는다 — 화면 회전이나 재구독 때 다시 발화한다.

## Screen.kt

세 가지만 들어간다.

**1. `XxxRoot`** (`internal`) — `koinViewModel`로 ViewModel을 붙이고, `state`를 구독하고, Event를 관찰하고, `onAction`에서 네비게이션 액션만 가로챈다.

```kotlin
@Composable
internal fun CatalogStudioRoot(
    studioId: Long,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: CatalogStudioViewModel = koinViewModel(parameters = { parametersOf(studioId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CatalogStudioScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogStudioAction.Navigation -> when (action) {   // else 없음 - 빠뜨리면 컴파일 에러
                    CatalogStudioAction.OnBackClick -> onBackClick()
                    is CatalogStudioAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}
```

**바깥 `when`의 `else`는 ViewModel 위임 전용이고, 안쪽 `when`에는 `else`를 쓰지 않는다.** 안쪽이 `Navigation`에 대해 exhaustive해야 새 네비게이션 액션을 추가했을 때 컴파일러가 잡아준다.

`state`는 `collectAsStateWithLifecycle()`로 구독한다(`collectAsState()` 아님).

**2. `XxxScreen`** (`private`) — `Scaffold` + 최상위 상태 분기(로딩 스켈레톤 / 에러 / 콘텐츠)만. 실제 콘텐츠는 컴포넌트 함수 호출 한두 줄로 위임한다. ViewModel도 Navigator도 모르는 순수 stateless 컴포저블이라 Preview가 그대로 된다.

**3. `@Preview` 함수들과 preview 전용 mock 데이터.** mock은 파일 하단에 top-level `private val`로 둔다. 로딩/에러/빈 상태처럼 실제로 확인해야 할 분기가 여러 개면 Preview도 분기마다 하나씩 만든다.

Screen.kt에 있으면 안 되는 것 → [`component-placement.md`](./component-placement.md).

## Action

`sealed interface`. 사용자가 화면에서 할 수 있는 행동 하나 = 케이스 하나. 파라미터 없으면 `data object`, 있으면 `data class`.

**Root가 처리하는 액션은 중첩 `Navigation` 인터페이스를 구현하게 한다.**

```kotlin
sealed interface CatalogStudioAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CatalogStudioAction

    data object OnBackClick : Navigation          // Root가 처리
    data class OnAnimeClick(val animeId: Long) : Navigation
    data object OnLoadMore : CatalogStudioAction  // ViewModel이 처리
}
```

액션 객체는 `CatalogStudioAction` 바로 아래에 그대로 두고 **상위 타입만 `Navigation`으로 바꾼다.** 이름이 `CatalogStudioAction.OnBackClick` 그대로라 호출부(`onAction(CatalogStudioAction.OnBackClick)`)를 하나도 안 고쳐도 된다.

이렇게 하면 Root의 안쪽 `when`이 `Navigation`에 대해 exhaustive해지고, **네비게이션 액션을 새로 추가하고 Root 분기를 깜빡하면 컴파일 에러가 난다.** 이 장치가 없으면 새 액션이 바깥 `else`로 새서 ViewModel의 `-> Unit`에 걸리고, 버튼이 조용히 아무 반응도 안 한다.

무엇을 `Navigation`에 넣나 — **ViewModel을 거치지 않고 Root가 끝내는 것 전부.** 뒤로가기, 다른 화면 이동, 외부 브라우저/공유 인텐트, OSS 라이선스 화면 등. 반대로 ViewModel이 상태를 바꾸거나 API를 부르는 액션은 넣지 않는다.

- 이름은 `On{무엇}{동사}` 형태로 통일한다 (`OnBackClick`, `OnRetryClick`, `OnLoadMore`).
- 선언 순서는 화면 위에서 아래 흐름대로. **QA 테스트케이스 ID가 이 순서를 따르므로**([`qa-testcase-format.md`](./qa-testcase-format.md)) 기존 케이스 사이에 끼워 넣지 말고 뒤에 추가한다.
- ViewModel의 `onAction`에서는 네비게이션 액션을 **한 줄로 받아넘긴다.** 액션마다 나열하지 않는다 — 새 네비게이션 액션이 추가돼도 ViewModel은 손댈 필요가 없다.
  ```kotlin
  is CatalogStudioAction.Navigation -> Unit // Root에서 처리한다.
  ```

## State

화면 하나 = `data class` 하나. `StateFlow<XxxState>`로 노출한다.

```kotlin
data class CatalogStudioState(
    val studioId: Long = 0L,
    val animes: List<Anime> = emptyList(),   // 컬렉션은 non-null
    val studioName: String? = null,          // 스칼라는 nullable 그대로
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
```

**1. 컬렉션은 State 진입 시점에 non-null로 정규화한다.** 모델까지는 `List<T>?`로 오지만([`dto-model-nullability.md`](./dto-model-nullability.md)), State에 담을 때 ViewModel이 `?: emptyList()`로 편다. 안 그러면 모든 Composable이 `?.`를 달아야 한다. 스칼라 값은 nullable을 그대로 들고 가서 view가 판단한다.

**2. 에러 필드에는 "사용자에게 그대로 보여줄 완성된 문구"만 담는다.**

`DataError`를 문자열로 바꾸는 경로는 `toDisplayMessage()`(`core/common/result/`) 하나뿐이다. **`error.toString()`을 쓰지 않는다** — `Api(code=500, message=null, reason=null)`이 그대로 화면에 렌더된다.

```kotlin
// Bad
.onFailure { error -> _state.update { it.copy(error = error.toString()) } }

// Good
.onFailure { error -> _state.update { it.copy(error = error.toDisplayMessage()) } }
```

`code`별로 다르게 처리해야 하는 케이스(다이얼로그, 특정 입력 필드 인라인 에러)는 그 `is DataError.Network.Api` 분기를 ViewModel에 직접 두고, 나머지 공통 케이스만 `toDisplayMessage()`에 위임한다.

**3. 로딩 실패와 "결과 없음"은 다른 상태다.** 지금 코드 일부는 `state.error ?: "검색조건에 맞는 결과가 없어요."`처럼 한 필드로 섞어 쓰는데, 재시도 버튼 유무가 달라야 하므로 새로 쓰는 화면에서는 섞지 않는다.

**4. 문구는 Compose에 한국어 리터럴로 직접 쓴다.** 다국어 계획이 없어서 `strings.xml`을 쓰지 않는 게 이 프로젝트의 기준이다. `core/ui/util/UiText.kt`는 이 결정 이전에 만들어진 것이라 신규 코드에서 쓰지 않는다.

## ViewModel

- 생성자로 화면 파라미터(NavKey에서 온 값)와 Repository를 받는다. Koin `viewModel { params -> ... }`로 등록한다.
- `_state` (`MutableStateFlow`) + `state` (`asStateFlow()`) 한 쌍.
- 진입 시 로딩은 `init { }`에서.
- 상태 갱신은 항상 `_state.update { it.copy(...) }`. `_state.value = ` 직접 대입은 안 쓴다.
- `PAGE_SIZE` 같은 상수는 `companion object`에 `private const val`로.

## 체크리스트

새 화면을 만들 때:

1. `api` 모듈에 NavKey 케이스를 추가하고, 진입에 필요한 파라미터를 담았는가?
2. `{screen}/`에 Action / State / ViewModel / Screen 4개를 만들었는가? Event는 **ViewModel이 목적지를 정하는 경우에만** 만들었는가?
3. Root가 처리할 액션을 `Navigation` 하위로 선언했는가? 안쪽 `when`에 `else`를 넣지 않았는가?
4. State의 컬렉션은 non-null인가? 에러 문구는 `toDisplayMessage()`를 거쳤는가?
5. `navigation/`에 `entry<XxxNavKey.Yyy>` + CONTENT_KEY 상수를, `di/`에 `viewModel { }`을 등록했는가?
6. `XxxScreen`이 stateless라 Preview가 그대로 도는가? 로딩/에러/빈 상태 Preview도 있는가?
7. 개발이 끝났으면 [`qa-testcase-format.md`](./qa-testcase-format.md) 기준으로 `docs/qa/testcases/{module}/{screen}.md`를 작성했는가?
