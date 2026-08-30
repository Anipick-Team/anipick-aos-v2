# Auth - 이메일 회원가입 (EmailSignupScreen)

패키지: `feature/auth/impl/email/signup`. 근거 파일: `EmailSignupAction.kt`, `EmailSignupState.kt`, `EmailSignupEvent.kt`, `EmailSignupViewModel.kt`, `EmailSignupScreen.kt`, `email/signup/components/EmailSignupFields.kt`, `EmailSignupAgreementSection.kt`, `AgreementCheckRow.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-EMAILSIGNUP-01 | 비밀번호 표시 토글(눈 아이콘) | - | 비밀번호 텍스트 마스킹 on/off | - | - | `EmailSignupViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02 | 가입하기 버튼 | 이메일 형식 정상 + 비밀번호 유효성 통과 + 필수 약관 3개 모두 동의 (버튼은 이 조건 아니면 비활성화) | 회원가입 성공 → 취향입력 화면으로 이동 | - | - | `EmailSignupViewModel.signUp` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02b | 가입하기 버튼 | 위와 동일 | 위와 동일 | 이미 가입된 이메일 (`code=109`) | 인라인 에러 텍스트(`emailError` = 서버 메시지 "이미 존재하는 이메일입니다.") | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02c | 가입하기 버튼 | 위와 동일 | 위와 동일 | 취약한 비밀번호 (`code=110`) | 인라인 에러 텍스트(`passwordError` = 서버 메시지 "8~16자의 영문 대/소문자, 숫자, 특수문자를 조합하여 입력해 주세요.") | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02d | 가입하기 버튼 | 위와 동일 | 위와 동일 | 약관 미동의 (`code=111`) | 인라인 에러 텍스트(`termsError` = 서버 메시지 "이용약관에 동의해 주세요.") | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02e | 가입하기 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02f | 가입하기 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-02g | 가입하기 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 스낵바 "알 수 없는 오류가 발생했습니다." | `EmailSignupViewModel.handleSignupFailure` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-03 | 이메일 입력란에 형식 안 맞는 값 입력 | - | 인라인 에러 텍스트 "올바른 이메일 형식이 아닙니다." (실시간) | - | - | `EmailSignupViewModel.observeEmailValidation` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-04 | 비밀번호 입력란에 값 입력 | - | 패턴(`PasswordPatternValidator`) 충족 시 라벨 옆 체크 아이콘(`isPasswordValid`) 표시 — 가입 버튼 활성 조건 중 하나 | - | - | `EmailSignupViewModel.observePasswordValidation` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-05 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `EmailSignupScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-06 | 모두 동의합니다 | - | 만14세 이상/이용약관/개인정보처리방침 3개 체크를 한 번에 토글(`isAgreeAll` 기준으로 전체 on 또는 전체 off) | - | - | `EmailSignupViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-07 | [필수] 만 14세 이상입니다 | - | 체크 on/off | - | - | `EmailSignupViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-08 | [필수] 이용약관에 동의합니다 | - | 체크 on/off | - | - | `EmailSignupViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-09 | 이용약관 자세히 보기(화살표 아이콘) | - | 외부 브라우저로 이용약관 페이지 이동 (`anipick.p-e.kr/terms.html`) | - | - | `EmailSignupRoot` (`Intent.ACTION_VIEW`) |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-10 | [필수] 개인정보 처리방침에 동의합니다 | - | 체크 on/off | - | - | `EmailSignupViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-EMAILSIGNUP-11 | 개인정보 처리방침 자세히 보기(화살표 아이콘) | - | 외부 브라우저로 개인정보 처리방침 페이지 이동 (`anipick.p-e.kr/privacy.html`) | - | - | `EmailSignupRoot` (`Intent.ACTION_VIEW`) |  |  |  |  |  |  |  |  |
