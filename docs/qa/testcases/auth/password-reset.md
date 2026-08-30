# Auth - 비밀번호 찾기 (인증 → 재설정)

비밀번호 찾기는 두 화면(패키지)에 걸쳐 진행된다: 이메일 인증(`PasswordVerificationScreen`) → 새 비밀번호 설정(`PasswordResetScreen`). 각 화면 액션 수가 적어 한 파일에 섹션으로 묶는다([`qa-testcase-format.md`](../../conventions/qa-testcase-format.md) 기준). 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

## 비밀번호 인증 (PasswordVerificationScreen)

패키지: `feature/auth/impl/password/verification`. 근거 파일: `PasswordVerificationAction.kt`, `PasswordVerificationState.kt`, `VerificationCodeRequestState.kt`, `PasswordVerificationEvent.kt`, `PasswordVerificationViewModel.kt`, `PasswordVerificationScreen.kt`, `password/verification/components/PasswordVerificationFields.kt`, `VerificationCodeRequestButton.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-PASSWORDVERIFICATION-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `PasswordVerificationScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02 | 인증번호 받기 버튼 | 이메일 형식 정상 (버튼 활성 조건) | 인증번호 발송 요청 + 버튼 "전송 중..." → "전송됨 mm:ss"(30초 재발송 쿨다운)로 전환, 발송 성공 시 인증번호 입력란 옆에 유효시간 카운트다운(180초) 시작 | - | - | `PasswordVerificationViewModel.requestVerificationCode` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02b | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | 가입된 계정 없음 (`code=112`) | 인라인 에러 텍스트(`emailError` = "가입된 계정이 없습니다. 이메일을 다시 확인해 주세요.") + 버튼은 즉시 "재발송하기"로 전환(쿨다운 취소) | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02c | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | 인증번호 관련 오류 (`code=113/114/115`) | 인라인 에러 텍스트(`codeError` = 서버 메시지) + 버튼은 즉시 "재발송하기"로 전환 | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02d | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | SNS로 간편 가입된 계정 (`code=122`) | 다이얼로그 "SNS로 간편 가입된 계정입니다." / "SNS로 로그인해주세요." (`showSnsLoginAlert`) + 버튼은 즉시 "재발송하기"로 전환 | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02e | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") + 버튼은 즉시 "재발송하기"로 전환 | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02f | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." + 버튼은 즉시 "재발송하기"로 전환 | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-02g | 인증번호 받기 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 스낵바 "알 수 없는 오류가 발생했습니다." + 버튼은 즉시 "재발송하기"로 전환 | `PasswordVerificationViewModel.handleSendFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-03 | 다음 버튼 | 이메일 형식 정상 + 인증번호 입력됨 (버튼 활성 조건) | 인증 성공 → 비밀번호 재설정 화면으로 이동(입력한 email 전달) | - | - | `PasswordVerificationViewModel.verifyCode` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-03b | 다음 버튼 | 위와 동일 | 위와 동일 | 인증 실패 (`Api` 에러, 보통 `code=114` 불일치/`115` 만료) | 인라인 에러 텍스트(`codeError` = 서버 메시지 그대로) | `PasswordVerificationViewModel.handleVerifyFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-03c | 다음 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." | `PasswordVerificationViewModel.handleVerifyFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-03d | 다음 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 스낵바 "알 수 없는 오류가 발생했습니다." | `PasswordVerificationViewModel.handleVerifyFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-04 | (다이얼로그) 닫기 | `showSnsLoginAlert`가 true인 상태 | 다이얼로그 닫힘(`OnAlertDismiss`) | - | - | `PasswordVerificationViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-05 | (다이얼로그) SNS 로그인 버튼 | `showSnsLoginAlert`가 true인 상태 | 다이얼로그 닫힘 + 소셜 로그인 화면으로 이동(`NavigateToLogin`) | - | - | `PasswordVerificationViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-06 | 인증번호 발송 후 이메일 입력란 수정 | 인증번호 발송 완료(카운트다운 진행 중이거나 쿨다운 중) 상태 | 인증번호 입력란 초기화 + 카운트다운/재발송 쿨다운 상태가 Idle로 리셋("인증번호 받기" 버튼으로 복귀) | - | - | `PasswordVerificationViewModel.resetCodeSessionIfActive` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDVERIFICATION-07 | 인증번호 유효시간(180초) 만료 | 인증번호 발송 성공 후 180초 경과, 그 사이 인증 시도 없음 | 인라인 에러 텍스트(`codeError` = "유효 시간이 만료되었습니다. 재발송 후 다시 시도해 주세요.") — 서버 `code=115` 문구와 동일하게 통일됨 | - | - | `PasswordVerificationViewModel.startCodeExpiryCountdown` |  |  |  |  |  |  |  |  |

## 비밀번호 재설정 (PasswordResetScreen)

패키지: `feature/auth/impl/password/reset`. 근거 파일: `PasswordResetAction.kt`, `PasswordResetState.kt`, `PasswordResetEvent.kt`, `PasswordResetViewModel.kt`, `PasswordResetScreen.kt`, `password/reset/components/PasswordResetFields.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-PASSWORDRESET-01 | 뒤로가기 | - | 이전 화면으로 이동 | - | - | `PasswordResetScreen` (Root) |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-02 | 비밀번호 표시 토글(눈 아이콘, 두 필드 각각) | - | 새 비밀번호/새 비밀번호 확인 두 필드 모두 마스킹 on/off (하나의 `showPassword` 상태를 공유) | - | - | `PasswordResetViewModel.onAction` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-03 | 비밀번호 변경 완료 버튼 | 새 비밀번호 유효성 통과 + 두 비밀번호 일치 (버튼 활성 조건) | 재설정 성공 → 이메일 로그인 화면으로 이동 | - | - | `PasswordResetViewModel.resetPassword` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-03b | 비밀번호 변경 완료 버튼 | 위와 동일 | 위와 동일 | `Api` 에러 | 인라인 에러 텍스트(`error` = 서버 메시지 그대로) | `PasswordResetViewModel.handleResetFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-03c | 비밀번호 변경 완료 버튼 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." | `PasswordResetViewModel.handleResetFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-03d | 비밀번호 변경 완료 버튼 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | 스낵바 "알 수 없는 오류가 발생했습니다." | `PasswordResetViewModel.handleResetFailure` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-04 | 새 비밀번호 입력란에 값 입력 | - | 패턴 충족 시 라벨 옆 체크 아이콘(`isNewPasswordValid`) 표시 | - | - | `PasswordResetViewModel.observeNewPasswordValidation` |  |  |  |  |  |  |  |  |
| AUTH-PASSWORDRESET-05 | 새 비밀번호 확인란에 값 입력 | - | 새 비밀번호와 일치하면 라벨 옆 체크 아이콘(`isPasswordMatch`) 표시 | - | - | `PasswordResetViewModel.observePasswordMatch` |  |  |  |  |  |  |  |  |

새 비밀번호 확인란에서 키보드 Done을 누르면(포커스가 있을 때) `isResetEnabled`가 true인 경우 자동으로 재설정이 트리거된다 — `PasswordResetFields.kt`의 `onKeyboardAction`. 다른 화면들과 동일 패턴이라 별도 케이스로 뽑지 않고 AUTH-PASSWORDRESET-03의 대체 트리거로 취급.
