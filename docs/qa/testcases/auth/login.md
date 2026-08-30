# Auth - 소셜 로그인 (LoginScreen)

패키지: `feature/auth/impl/login`. 근거 파일: `LoginAction.kt`, `LoginEvent.kt`, `LoginViewModel.kt`, `LoginScreen.kt`, `login/components/LoginActions.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

카카오/구글 버튼은 텍스트 라벨 없는 이미지 버튼(`contentDescription`만 있음)이라 "이벤트/트리거" 칸은 그 설명으로 적는다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-LOGIN-01 | 카카오 로그인 버튼(이미지 버튼) | 카카오톡 앱 설치됨 | 카카오톡 앱으로 로그인 → 성공 시 `reviewCompletedYn`에 따라 홈 또는 취향입력 화면으로 이동 | 사용자가 카카오톡 로그인 화면에서 직접 취소 | - (조용히 종료, 스낵바 없음) | `LoginViewModel.requestKakaoToken` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-01b | 카카오 로그인 버튼 | 카카오톡 앱 설치됨 | 위와 동일 | 카카오톡 로그인 실패(취소 아님) | 카카오계정(웹) 로그인으로 자동 재시도 | `LoginViewModel.requestKakaoToken` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-01c | 카카오 로그인 버튼 | - | 위와 동일 | `authRepository.loginWithKakao` 실패 | `NO_INTERNET` → 스낵바 "네트워크 연결을 확인해주세요." / 그 외 → 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") | `LoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-02 | 구글 로그인 버튼(이미지 버튼) | - | 구글 로그인 → 성공 시 `reviewCompletedYn`에 따라 홈 또는 취향입력 화면으로 이동 | 사용자가 직접 취소(`GetCredentialCancellationException`) 또는 그 외 credential 실패(`GetCredentialException`) | - (조용히 종료, 스낵바 없음 — 카카오와 달리 자동 재시도 경로 없음) | `LoginViewModel.requestGoogleIdToken` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-02b | 구글 로그인 버튼 | - | 위와 동일 | `authRepository.loginWithGoogle` 실패 | `NO_INTERNET` → 스낵바 "네트워크 연결을 확인해주세요." / 그 외 → 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") | `LoginViewModel.handleLoginFailure` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-03 | 이메일 로그인 | - | 이메일 로그인 화면으로 이동 | - | - | `LoginScreen` (Root에서 처리, ViewModel 안 거침) |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-04 | 이메일 회원가입 | - | 이메일 회원가입 화면으로 이동 | - | - | `LoginScreen` |  |  |  |  |  |  |  |  |
| AUTH-LOGIN-05 | 로그인에 문제가 있으신가요? | - | 외부 브라우저로 문의 폼(Google Forms)으로 이동 | - | - | `LoginRoot` (`Intent.ACTION_VIEW`) |  |  |  |  |  |  |  |  |

## 개발 누락/참고 사항

- **code 133("로컬 계정으로 소셜 로그인 시도")이 이 화면에서 전용 처리가 안 돼 있다.** `LoginViewModel.handleLoginFailure`는 `NO_INTERNET` 외엔 전부 같은 스낵바 경로라, 이메일 회원가입 계정으로 소셜 로그인을 시도하면 서버가 133을 내려줘도 `EmailLoginViewModel`의 132(탈퇴 계정) 다이얼로그 같은 전용 UX 없이 그냥 스낵바로만 뜬다. `error-codes.md` 79~80행에 이미 같은 내용이 기록돼 있음 — 새로 발견한 게 아니라 기존에 known gap으로 남아있던 것.
