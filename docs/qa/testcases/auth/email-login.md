# Auth - 이메일 로그인 (EmailLoginScreen)

패키지: `feature/auth/impl/email/login`. 근거 파일: `EmailLoginAction.kt`, `EmailLoginState.kt`, `EmailLoginEvent.kt`, `EmailLoginViewModel.kt`, `EmailLoginScreen.kt`, `email/login/components/EmailLoginFields.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-EMAILLOGIN-01 | 비밀번호 표시 토글(눈 아이콘) | - | 비밀번호 텍스트 마스킹 on/off | - | - | `EmailLoginViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-02 | 로그인 버튼 | 이메일 형식 정상 + 비밀번호 입력됨 (버튼은 이 조건 아니면 비활성화) | 로그인 성공 → `reviewCompletedYn`에 따라 홈/취향입력 이동 | - | - | `EmailLoginViewModel.login` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-02b | 로그인 버튼 | 위와 동일 | 위와 동일 | 탈퇴 후 30일 이내 재가입 시도 계정 (`code=132`) | 다이얼로그 "탈퇴된 계정입니다." / "자세한 사항은 고객센터로 문의해 주세요.\nteamanipick@gmail.com" (`showAccountDeletedDialog`) | `EmailLoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-02c | 로그인 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러(예: 비밀번호 불일치 `code=101`) | 인라인 에러 텍스트(`loginError` = 서버 에러 메시지 그대로) — 스낵바 아님 | `EmailLoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-02d | 로그인 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." — 인라인 에러 아님 | `EmailLoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-02e | 로그인 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 인라인 에러 텍스트 "알 수 없는 오류가 발생했습니다." | `EmailLoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-03 | 이메일 입력란에 형식 안 맞는 값 입력 | - | 인라인 에러 텍스트 "올바른 이메일 형식이 아닙니다." (스낵바 아님, 실시간) | - | - | `EmailLoginViewModel.observeEmailValidation` |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-04 | 이메일 회원가입 | - | 이메일 회원가입 화면으로 이동 | - | - | `EmailLoginScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-05 | 비밀번호 찾기 | - | 비밀번호 찾기(인증) 화면으로 이동 | - | - | `EmailLoginScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-06 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `EmailLoginScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-EMAILLOGIN-07 | (다이얼로그) 닫기 | `showAccountDeletedDialog`가 true인 상태 | 다이얼로그 닫힘(`OnDialogDismiss`) | - | - | `EmailLoginViewModel.onAction` |  |  |  |  |  |  |  |  |

비밀번호 입력란에서 키보드 Done을 누르면(포커스가 있을 때) `isLoginEnabled`가 true인 경우 자동으로 로그인이 트리거된다 — `EmailLoginFields.kt`의 `onKeyboardAction`. 회원가입 화면도 동일 패턴이라 별도 케이스로 뽑지 않고 AUTH-EMAILLOGIN-02의 대체 트리거로 취급.
