# AniPick_v2

Jetpack Compose + Navigation 3 + Koin 기반 멀티모듈 안드로이드 앱. 서비스 소개와 기술 스택은 [README.md](./README.md)를 본다.

## 모듈 구조

```
app/                      # NavDisplay, 바텀 네비게이션, Application
core/
├── common/               # Result, DataError, toDisplayMessage
├── data/                 # Repository (인터페이스 + Impl)
├── database/ datastore/  # Room, DataStore
├── designsystem/         # 테마·토큰·원시 컴포넌트 (도메인 모델 모름)
├── model/                # 도메인 모델
├── navigation/           # Navigator, NavigationState
├── network/              # Ktor DataSource + 응답 DTO
└── ui/                   # 도메인 모델을 아는 공용 컴포넌트/이펙트/유틸
feature/{module}/api/     # NavKey만 — 다른 feature는 여기에만 의존한다
feature/{module}/impl/    # 화면 구현 — impl끼리는 서로 참조하지 않는다
```

의존 방향: `app → feature/impl → feature/api, core/*`. `core/data`는 `core/network`·`core/model`에 의존하고, 그 반대는 없다.

## 작업 전에 읽을 것

작업 종류에 해당하는 문서를 **코드를 고치기 전에** 확인한다. 여기 있는 규칙이 기존 코드와 어긋나 보이면, 문서가 기준이고 그 코드가 아직 정리되지 않은 것이다.

| 무슨 작업인가 | 문서 |
|---|---|
| 새 화면 추가, ViewModel/State/Action 설계, 네비게이션 연결 | [docs/conventions/screen-structure.md](./docs/conventions/screen-structure.md) |
| 커진 컴포넌트를 어디로 뺄지, `core/ui` vs `core/designsystem` 판단 | [docs/conventions/component-placement.md](./docs/conventions/component-placement.md) |
| 새 API 응답 DTO/모델 추가, 매퍼 작성 | [docs/conventions/dto-model-nullability.md](./docs/conventions/dto-model-nullability.md) |
| 주석 작성 | [docs/conventions/comment-style.md](./docs/conventions/comment-style.md) |
| 화면 개발 완료 후 QA 테스트케이스 작성 | [docs/conventions/qa-testcase-format.md](./docs/conventions/qa-testcase-format.md) |
| 서버 비즈니스 에러 code별 노출 메시지 확인 | [docs/reference/error-codes.md](./docs/reference/error-codes.md) |

## 자주 걸리는 것

- **에러 문구는 `toDisplayMessage()`를 거친다.** `error.toString()`을 State에 넣으면 `Api(code=500, ...)`이 그대로 화면에 렌더된다.
- **응답 DTO 필드는 전부 `Type? = null`.** 서버가 명시적 `null`을 내려주고 `coerceInputValues`를 껐기 때문에, non-null로 두면 그 필드가 null인 응답에서 역직렬화가 터진다.
- **문구는 Compose에 한국어 리터럴로 직접 쓴다.** 다국어 계획이 없어 `strings.xml`을 쓰지 않는다.
- **빈 `XxxEvent.kt`를 미리 만들지 않는다.** Event는 ViewModel이 비동기 결과로 목적지를 정할 때만 만든다.
- **Root가 처리하는 액션은 `XxxAction.Navigation` 하위로 선언한다.** Root의 안쪽 `when`에 `else`를 넣으면 새 네비게이션 액션이 조용히 무반응이 되는 걸 컴파일러가 못 잡는다.

## 검증

UI 변경은 **컴파일 + Compose Preview로 확인한다.** 에뮬레이터/실기기를 띄워 직접 눌러보지 않는다 — 실기기 확인이 필요하면 사용자가 직접 한다.

```bash
./gradlew compileDebugKotlin      # 전체 컴파일
./gradlew :feature:catalog:impl:compileDebugKotlin
```
