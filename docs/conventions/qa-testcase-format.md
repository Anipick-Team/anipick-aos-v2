# QA 테스트케이스 문서 컨벤션

화면 단위로 "어떤 이벤트를 트리거하면 뭐가 보여야 하는가"를 표 형식으로 정리해서, 회귀 확인용 체크리스트이자 나중에 엑셀로 뽑을 원본으로 쓴다. 이 문서는 그 표의 컬럼/단위/작성 기준을 정의한다 — 화면을 만들 때마다 형식을 다시 물어보지 않기 위한 기준.

## 검증 방법

**코드 읽기(정적 검토) 기준으로 작성한다.** 에뮬레이터/실기기를 띄워서 직접 눌러보며 검증하지 않는다 — `Action` sealed interface, `ViewModel.onAction`/이벤트 발생 지점, `Screen.kt`의 상태 분기(스낵바/다이얼로그/인라인 에러 텍스트 렌더링)를 읽고 "이 이벤트가 발생하면 코드상 이 결과가 나온다"를 표에 적는다. 실제 기기 확인이 필요하면 사용자가 직접 하고, 결과를 알려주면 표를 갱신한다.

API 호출이 실패해서 서버가 비즈니스 에러 코드를 내려주는 케이스는 [`docs/reference/error-codes.md`](../reference/error-codes.md)에서 code별 노출 메시지/클라이언트 처리 여부를 먼저 확인한다 — 화면의 `ViewModel`이 어떤 `code`를 분기하는지 찾았으면 그 표에서 정확한 메시지를 그대로 가져와 "실패 시 UI" 칸에 쓴다.

## 문서 단위: 화면 하나 = 파일 하나

```
docs/qa/testcases/{module}/{screen}.md
```

예: `docs/qa/testcases/auth/login.md`, `docs/qa/testcases/auth/email-login.md`. 모듈/화면 이름은 `feature/{module}/impl/{screen}` 패키지 경로를 그대로 따른다. 화면이 너무 작으면(이벤트 2~3개) 같은 모듈의 관련 화면과 한 파일에 섹션(`##`)으로 묶어도 된다 — 예: `auth/password-reset.md` 안에 요청/인증/재설정 3단계를 섹션으로.

## 테스트케이스 단위: "관찰 가능한 이벤트 하나"

그 화면의 `XxxAction` sealed interface에 있는 액션 하나하나가 기본 후보다. 그중에서:

- **단순 네비게이션/외부 인텐트** (`Root`에서 바로 처리하고 ViewModel을 안 거치는 것 — 예: 뒤로가기, 문의 페이지 이동, 다른 화면으로 이동하는 링크 클릭)는 "예상 결과"에 이동할 화면 이름만 적으면 끝. 성공/실패 분기가 없다.
- **API를 호출하는 액션** (로그인 시도, 폼 제출 등)은 **성공 케이스 1행 + 실패 케이스마다 1행**으로 나눈다. `ViewModel`의 `onFailure`/`handleXxxFailure` 안에서 분기하는 `DataError` 케이스 수만큼 행이 생긴다 (예: `NO_INTERNET` → 스낵바, `Api(code=132)` → 다이얼로그, 그 외 `Api` → 인라인 에러 텍스트).
- **순수 로컬 상태 토글** (비밀번호 보이기/숨기기, 탭 전환 등 네트워크 없는 것)은 API 콜이 없으니 "예상 결과"에 상태 변화만 짧게 적는다. 굳이 화면 전체를 다시 그릴 필요는 없고 한 줄로 충분.
- ViewModel이 **자체적으로 관찰하는 부수효과**(예: 이메일 입력값 유효성 검사 → 인라인 에러, 여러 필드 조합 → 버튼 활성화)도 액션은 아니지만 후보에 포함한다 — 사용자 입장에서는 "이 상태가 되면 이게 뜬다"는 관찰 가능한 동작이기 때문.
- **다른 화면에도 자동 반영돼야 하는 값을 바꾸는 액션**은 그 화면 안에서 성공/실패 분기와 별개로, **"화면 간 자동 반영 확인" 행을 반드시 하나 더 추가한다.** 바로 아래 절 참고.

## 화면 간 자동 반영(구독) 케이스

닉네임/이메일처럼 여러 화면이 동시에 보여주는 값은 한 곳에서 바꾸면 다른 화면도 재진입이나 새로고침 없이 같이 바뀌어야 한다. 이 프로젝트는 그걸 `core/data`의 Repository가 `Flow`로 노출하고 여러 ViewModel이 `.collect`/`combine`으로 구독하는 방식으로 구현한다 — 이 구독 관계 자체가 놓치기 쉬운 회귀 포인트라 별도 케이스로 명시한다.

**찾는 방법:**

1. 값을 바꾸는 액션(예: `SettingDetailViewModel.save()`가 `userRepository.updateNickname(...)` 호출)을 발견하면, 그 값이 Repository 인터페이스에서 `Flow`로도 노출되는지 확인한다.
   ```
   grep -n "Flow<" core/data/src/main/java/com/jparkbro/core/data/user/UserRepository.kt
   # val nickname: Flow<String?>, val email: Flow<String?>
   ```
2. `RepositoryImpl`에서 그 값을 쓰는 함수가 실제로 그 Flow의 소스(보통 DataStore)에 같이 쓰는지 확인한다 — 안 쓰면 애초에 반영 안 되는 게 정상이니 이 케이스 자체가 필요 없다.
   ```
   # UserRepositoryImpl.updateNickname: userNetworkDataSource.updateNickname(...).onSuccess { userDataStore.saveNickname(nickname) }
   ```
3. 그 Flow를 구독하는 **모든** 화면을 찾는다 — 짐작하지 말고 실제로 grep한다. 하나라도 빠뜨리면 그 화면만 회귀가 나도 못 잡는다.
   ```
   grep -rn "userRepository.nickname\|userRepository.email" --include="*.kt" feature/
   ```

**어느 파일에 쓰는가:** 값을 **바꾸는** 화면(위 예시면 `SettingDetail`)의 md에 쓴다 — 트리거가 거기 있으니까. **읽기만** 하는 화면(`SettingMain`, `HomeMain` 등)의 md가 나중에 따로 생기면, 거기엔 이 케이스를 복사하지 말고 "이 값이 어디서 바뀌는지"만 근거에 한 줄 링크한다 — 같은 케이스가 여러 파일에 중복되는 걸 막기 위함. 다만 "이 화면이 떠 있는 상태에서 다른 화면이 값을 바꿔도 재진입 없이 바뀌는가"는 **읽는 화면 쪽에서만 검증 가능한 별개의 관찰**이라, 읽는 화면의 md에도 그 관점으로 케이스를 하나 둔다(중복이 아니라 다른 관점).

**컬럼 작성 요령:**
- 이벤트/트리거: 그 값을 바꾸는 액션 그대로 (예: "저장" 버튼) + "(화면 간 자동 반영 확인)"을 덧붙여 성공 케이스 행과 구분한다.
- 예상 결과: 어느 화면의 어느 UI 요소가 갱신되는지 화면 이름 + 컴포넌트/필드명까지 전부 나열한다. "다른 화면도 갱신된다" 정도로 뭉뚱그리지 않는다.
- 근거: 쓰는 곳(`XxxRepositoryImpl.updateXxx`)과 읽는 곳(구독하는 ViewModel 함수 전부)을 둘 다 적는다.

실제로 적용한 예시는 `docs/qa/testcases/mypage/setting-detail.md`의 `MYPAGE-SETTINGDETAIL-NICKNAME-04`/`-EMAIL-07`(쓰는 쪽 관점)과 `docs/qa/testcases/mypage/setting-main.md`의 `MYPAGE-SETTINGMAIN-17`(읽는 쪽 관점) 참고 — `nickname`은 `SettingMain`/`HomeMain`/`HomeDetail`(추천 탭) 3곳이, `email`은 `SettingMain` 1곳만 구독한다.

## 컬럼

세 그룹으로 나뉜다. **케이스 정의**는 케이스를 쓸 때 코드 보고 채우는 것(이후 거의 안 바뀜), 나머지 둘은 실제로 테스트를 돌릴 때 채우는 것인데 **채우는 빈도가 달라서 위치를 다르게 둔다**:

- **실행 결과**(테스트 결과/특이사항)는 케이스 하나 확인할 때마다 그 자리에서 바로 채우는 값이라, "예상 결과"/"실패 시 UI"와 같은 화면(스크롤 없이)에 보이도록 **근거 바로 다음**에 둔다.
- **실행 메타**(테스터/테스트 일자/앱 버전/OS/OS 버전/기종)는 케이스 하나하나가 아니라 그 테스트 세션 전체에 한 번만 정해지는 값이라(같은 사람이 같은 기기로 그 세션의 모든 케이스를 도니까), 매 행 반복 입력할 필요 없이 맨 뒤에 몰아서 한 번에 채운다.

### 케이스 정의

| 컬럼 | 설명 |
|---|---|
| ID | `{MODULE}-{SCREEN}-{NN}` (2자리, `01`부터). 예: `AUTH-LOGIN-01` |
| 이벤트/트리거 | 사용자가 하는 행동. 버튼 이름 그대로 (코드 주석 아님, 화면에 보이는 라벨) |
| 사전조건 | 이 케이스가 성립하려면 필요한 상태. 없으면 `-` |
| 예상 결과 | 성공 시 무슨 일이 일어나는가 — 이동할 화면, 상태 변화, API 호출 등 |
| 실패 케이스 | 실패를 유발하는 조건. 성공/네비게이션 전용 행이면 `-` |
| 실패 시 UI | 스낵바 문구(정확한 텍스트) / 인라인 에러 텍스트(정확한 텍스트) / 다이얼로그 제목+메시지 중 해당하는 것. 없으면 `-` — "에러는 나는데 UI로 아무것도 안 보여준다"도 유의미한 값이니 빈 칸으로 두지 말고 명시 |
| 근거 | 어느 파일/함수를 보고 판단했는지. 예: `EmailLoginViewModel.handleLoginFailure` |

### 실행 결과 (케이스마다 채움, 근거 바로 다음에 위치)

| 컬럼 | 설명 |
|---|---|
| 테스트 결과 | `Pass` / `Fail` / `Blocked`(선행 케이스 실패로 진행 불가) / `-`(미실행) |
| 특이사항 | `Fail`일 때 실제로 뭐가 어떻게 잘못됐는지, 스크린샷/영상 링크 등. `Pass`면 보통 `-` |

### 실행 메타 (세션당 한 번, 맨 뒤에 위치)

케이스를 처음 쓸 때는 전부 빈 칸으로 둔다 — 코드 리뷰만으로는 채울 수 없는, 실제로 돌려본 사람이 채우는 값들이다.

| 컬럼 | 설명 |
|---|---|
| 테스터 | 실행한 사람 이름 |
| 테스트 일자 | `YYYY-MM-DD` |
| 앱 버전 | 테스트한 빌드의 `BuildConfig.APP_VERSION`(버전명) 또는 versionCode. 이게 없으면 "그때 됐었는데 지금 안 된다"는 회귀를 구분할 수가 없어서, 요청받은 목록에 없지만 추가했다 |
| OS | `Android` / `iOS` |
| OS 버전 | 예: `14`, `17.2` |
| 기종 | 예: `Pixel 8`, `Galaxy S24`, `iPhone 15` |

## ID 규칙

`MODULE`은 `feature/{module}` 이름을 대문자로(`AUTH`, `CATALOG`, `HOME` ...), `SCREEN`은 화면 패키지 이름을 케밥 없이 축약(`LOGIN`, `EMAILLOGIN`, `CATALOGANIME` ...).

- **번호는 `01`부터**, 같은 화면 안에서는 `Action` 선언 순서를 그대로 따라 매긴다.
- 나중에 액션이 추가되면 **그 뒤에 이어 붙이고, 기존 번호는 바꾸지 않는다**(리뷰 히스토리 꼬임 방지).
- **같은 액션의 실패 분기는 새 숫자를 쓰지 않고 알파벳 서픽스를 붙인다** — `AUTH-EMAILLOGIN-02`(성공), `-02b`, `-02c`, `-02d`... 이렇게 해야 나중에 실패 분기가 하나 늘어도 뒤 번호가 전부 밀리지 않는다. 서픽스는 `b`부터 시작한다(성공 케이스가 `a` 자리).
- 한 화면에서 성공 케이스가 여러 갈래인 경우(예: 소셜 로그인 카카오/구글)는 서픽스가 아니라 각각 다른 번호를 쓴다 — 서로 다른 액션이기 때문.

## 작성 예시: `auth/login.md`

```markdown
# Auth - 소셜 로그인 (LoginScreen)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-LOGIN-01 | 카카오 계정으로 계속하기 | 카카오톡 앱 설치됨 | 카카오톡 로그인 → 성공 시 `reviewCompletedYn`에 따라 홈 또는 취향입력 화면으로 이동 | 사용자가 로그인 화면에서 직접 취소 | - (조용히 종료, 스낵바 없음) | `LoginViewModel.requestKakaoToken` | | | | | | | | |
| AUTH-LOGIN-01b | 카카오 계정으로 계속하기 | 카카오톡 앱 설치됨 | 위와 동일 | 카카오톡 로그인 실패(취소 아님) | 카카오계정(웹) 로그인으로 자동 재시도 | `LoginViewModel.requestKakaoToken` | | | | | | | | |
| AUTH-LOGIN-01c | 카카오 계정으로 계속하기 | - | 위와 동일 | `authRepository.loginWithKakao` 실패 | `NO_INTERNET` → 스낵바 "네트워크 연결을 확인해주세요." / 그 외 → 스낵바 (API 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") | `LoginViewModel.handleLoginFailure` | | | | | | | | |
| AUTH-LOGIN-02 | 구글 계정으로 계속하기 | - | AUTH-LOGIN-01/01c와 동일한 흐름(`googleLogin`) | 위와 동일 | 위와 동일 | `LoginViewModel.googleLogin` | | | | | | | | |
| AUTH-LOGIN-03 | 로그인에 문제가 있으신가요? | - | 문의 페이지로 이동 | - | - | `LoginScreen` (Root에서 처리, ViewModel 안 거침) | | | | | | | | |
| AUTH-LOGIN-04 | 이메일 회원가입 | - | 이메일 회원가입 화면으로 이동 | - | - | `LoginScreen` | | | | | | | | |
| AUTH-LOGIN-05 | 이메일 로그인 | - | 이메일 로그인 화면으로 이동 | - | - | `LoginScreen` | | | | | | | | |
```

## 작성 예시: 실패 케이스가 갈리는 화면 — `auth/email-login.md`

```markdown
# Auth - 이메일 로그인 (EmailLoginScreen)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-EMAILLOGIN-01 | 비밀번호 표시 토글(눈 아이콘) | - | 비밀번호 텍스트 마스킹 on/off | - | - | `EmailLoginViewModel.onAction` | | | | | | | | |
| AUTH-EMAILLOGIN-02 | 로그인 버튼 | 이메일 형식 정상 + 비밀번호 입력됨 (버튼은 이 조건 아니면 비활성화) | 로그인 성공 → `reviewCompletedYn`에 따라 홈/취향입력 이동 | - | - | `EmailLoginViewModel.login` | | | | | | | | |
| AUTH-EMAILLOGIN-02b | 로그인 버튼 | 위와 동일 | 위와 동일 | 탈퇴한 계정 (`DataError.Network.Api(code=132)`) | 다이얼로그 표시(`showAccountDeletedDialog`) | `EmailLoginViewModel.handleLoginFailure` | | | | | | | | |
| AUTH-EMAILLOGIN-02c | 로그인 버튼 | 위와 동일 | 위와 동일 | 비밀번호 틀림 등 그 외 `Api` 에러 | 인라인 에러 텍스트(`loginError` = 서버 에러 메시지 그대로) — 스낵바 아님 | `EmailLoginViewModel.handleLoginFailure` | | | | | | | | |
| AUTH-EMAILLOGIN-02d | 로그인 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." — 인라인 에러 아님 | `EmailLoginViewModel.handleLoginFailure` | | | | | | | | |
| AUTH-EMAILLOGIN-02e | 로그인 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 인라인 에러 텍스트 "알 수 없는 오류가 발생했습니다." | `EmailLoginViewModel.handleLoginFailure` | | | | | | | | |
| AUTH-EMAILLOGIN-03 | 이메일 입력란에 형식 안 맞는 값 입력 | - | 인라인 에러 텍스트 "올바른 이메일 형식이 아닙니다." (스낵바 아님, 실시간) | - | - | `EmailLoginViewModel.observeEmailValidation` | | | | | | | | |
| AUTH-EMAILLOGIN-04 | 회원가입 | - | 이메일 회원가입 화면으로 이동 | - | - | `EmailLoginScreen` | | | | | | | | |
| AUTH-EMAILLOGIN-05 | 비밀번호 찾기 | - | 비밀번호 찾기 화면으로 이동 | - | - | `EmailLoginScreen` | | | | | | | | |
| AUTH-EMAILLOGIN-06 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `EmailLoginScreen` | | | | | | | | |
| AUTH-EMAILLOGIN-07 | (다이얼로그) 확인/닫기 | `showAccountDeletedDialog`가 true인 상태 | 다이얼로그 닫힘(`OnDialogDismiss`) | - | - | `EmailLoginViewModel.onAction` | | | | | | | | |
```

이 두 예시가 실제 이 프로젝트의 `LoginViewModel`/`EmailLoginViewModel` 코드를 그대로 읽고 작성한 것이니, 새 화면 작성할 때 이 두 파일을 템플릿으로 복사해서 시작하면 된다.

## 엑셀로 뽑을 때

> 실제로 뽑을 때는 *"docs/qa/testcases/{module}/{screen}.md를 qa-testcase-format.md 기준으로 엑셀로 뽑아줘"* 라고 하면 된다.

지금은 화면마다 이 형식의 마크다운 표로 쌓아두고, 한 번에 모아서 뽑을 때(`openpyxl`) 화면 하나 = 시트 하나로 변환한다. 표 스키마가 마크다운과 엑셀에서 완전히 같아야 자동 변환 스크립트를 짤 수 있으니, 컬럼 이름/순서를 임의로 바꾸지 않는다.

`docs/qa/generate_xlsx.py`로 뽑고, **결과 xlsx는 저장소 밖 `/Users/jparkbro/Documents/WorkSpace/QA`에 저장한다** (레포 안에 커밋하지 않는다 — 소스 오브 트루스는 이 md들이고, xlsx는 그때그때 다시 뽑을 수 있는 산출물이다). 예:

```
python3 docs/qa/generate_xlsx.py -o "/Users/jparkbro/Documents/WorkSpace/QA/AniPick_QA_TestCases.xlsx"
python3 docs/qa/generate_xlsx.py testcases/mypage -o "/Users/jparkbro/Documents/WorkSpace/QA/mypage_setting_qa.xlsx"
```

## 체크리스트

새 화면 테스트케이스를 쓸 때:

1. 그 화면의 `XxxAction.kt`를 열고 선언 순서대로 ID를 매긴다.
2. Root에서 바로 처리하는 액션(네비게이션/인텐트)인지, ViewModel로 가는 액션인지 구분한다 — 전자는 성공행 하나로 끝.
3. ViewModel로 가는 액션이면 `onAction`에서 호출하는 함수까지 따라가서, `onFailure`/`handleXxxFailure` 안의 `when` 분기 수만큼 실패 행을 나눈다.
4. 스낵바(`GlobalSnackbarManager.showSnackbar`)와 인라인 에러(state의 `xxxError` 필드 → `Screen.kt`에서 `Text`로 렌더링)를 구분해서 "실패 시 UI" 컬럼에 정확히 어느 쪽인지, 정확한 문구를 적는다.
5. 로컬 상태만 바뀌는 토글/유효성 검사도 빠뜨리지 않는다 — API를 안 부른다고 테스트케이스가 아닌 게 아니다.
6. "실행 기록" 8개 컬럼(테스트 결과/특이사항/테스터/테스트 일자/앱 버전/OS/OS 버전/기종)은 케이스를 처음 쓸 때는 전부 빈 칸으로 둔다 — 코드만 보고는 채울 수 없는 값들이다.
7. 성공 케이스가 Repository에 값을 쓰는 액션이면(`onSuccess { xxxRepository.updateXxx(...) }` 형태), 그 Repository 인터페이스에 같은 값이 `Flow`로도 노출되는지 확인한다 — 노출된다면 "화면 간 자동 반영" 절 기준으로 그걸 구독하는 화면들을 grep해서 케이스를 추가한다.
