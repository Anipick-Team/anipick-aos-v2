# MyPage - 설정 상세 (SettingDetailScreen)

패키지: `feature/mypage/impl/setting/detail`. 근거 파일: `SettingDetailAction.kt`, `SettingDetailState.kt`, `SettingDetailViewModel.kt`, `SettingDetailScreen.kt`.

화면 하나가 `SettingDetailType`(Nickname / Email / Password / Withdrawal) 4가지 모드를 `when(state.type)`로 분기해서 보여준다. `OnSaveClick` 등 액션은 공유하지만 모드마다 동작이 완전히 다르므로, 공통 섹션 + 모드별 섹션으로 나눈다. 에러 메시지는 [`error-codes.md`](../../../reference/error-codes.md) 참고.

## 공통

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGDETAIL-01 | 뒤로가기(상단 앱바) | - | 이전 화면(설정 메인)으로 이동 | - | - | `SettingDetailScreen` (Root에서 처리) |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-02 | 비밀번호 표시 토글(눈 아이콘) | `type`이 Email 또는 Password (이 아이콘이 있는 필드가 있는 모드) | 비밀번호류 필드 마스킹 on/off (`showPassword`) — 같은 화면의 모든 비밀번호 필드에 공통 적용 | - | - | `SettingDetailViewModel.onAction` |  |  |  |  |  |  |  |  |

## 닉네임 변경 (`type = Nickname`)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGDETAIL-NICKNAME-01 | "저장" 버튼 | 새 닉네임 입력됨(`isChangeNicknameEnabled`) — 미입력 시 버튼 비활성화 | 성공 시 `SaveSuccess` 이벤트 → 이전 화면(설정 메인)으로 이동 | - | - | `SettingDetailViewModel.save` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-NICKNAME-02 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 116(형식 오류)/117(중복)/118(미입력) | 인라인 에러 텍스트(`nicknameError`, `AniPickErrorText`) — 서버 메시지 그대로. 예: "이미 사용 중인 닉네임입니다." | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-NICKNAME-03 | "저장" 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 코드 / `NO_INTERNET` / 알 수 없는 에러 | 스낵바 — `Api` 그 외는 "요청을 처리하지 못했습니다."(또는 서버 메시지), `NO_INTERNET`은 "네트워크 연결을 확인해주세요.", 알 수 없는 에러는 "알 수 없는 오류가 발생했습니다." | `handleSaveFailure`, `applyFieldError` else 분기 |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-NICKNAME-04 | "저장" 버튼 (화면 간 자동 반영 확인) | NICKNAME-01과 동일하게 저장 성공 | 새 닉네임이 `userDataStore.saveNickname`을 거쳐 `nickname` Flow로 나가서, 재진입/새로고침 없이 (1) 설정 메인(`SettingMainScreen`) "닉네임 변경" 행 표시값, (2) 홈 메인(`HomeMainScreen`) "오늘의 추천작, {닉네임}님..." 타이틀, (3) 홈 추천 상세(`HomeDetailScreen`, 추천 타입이고 `basedOnAnimeId`가 없는 경우)의 같은 타이틀이 전부 새 닉네임으로 갱신된다. 세 화면이 백스택/다른 탭에 살아있는 상태에서 확인하는 게 가장 확실하다(단순히 재진입만 하면 화면이 새로 로드되면서 값이 맞는 게 당연해서 이 케이스의 의미가 옅어짐) | - | - | 쓰는 곳: `UserRepositoryImpl.updateNickname` / 읽는 곳: `SettingMainViewModel.observeUser`, `HomeMainViewModel.observeNickname`, `HomeDetailViewModel.observeNickname` (렌더링은 공통 `RecommendationSectionTitle`) |  |  |  |  |  |  |  |  |

## 이메일 변경 (`type = Email`)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGDETAIL-EMAIL-01 | (자동) 새 이메일 입력란에 타이핑 | - | 형식 유효성(`isNewEmailValid`)이 실시간으로 갱신되어 저장 버튼 활성화 여부에 반영됨 — 이 시점엔 인라인 에러 텍스트가 뜨지 않는다(형식 체크는 버튼 활성화로만 표현, 에러 텍스트는 저장 실패 시(code 103)에만 뜸) | - | - | `SettingDetailViewModel.observeNewEmailValidation` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-02 | "저장" 버튼 | 새 이메일 형식 유효 + 비밀번호 입력됨(`isChangeEmailEnabled`) | 성공 시 `SaveSuccess` → 이전 화면으로 이동 | - | - | `SettingDetailViewModel.save` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-03 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 102(미입력)/103(형식 오류)/109(중복) | 인라인 에러 텍스트(`emailError`) — 새 이메일 필드 아래. 예: "이미 존재하는 이메일입니다." | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-04 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 106(비밀번호 불일치) | 인라인 에러 텍스트(`emailPasswordError`) — 비밀번호 필드 아래. 서버가 code 106에 메시지를 안 내려주면 빈 텍스트가 뜰 수 있음(`error-codes.md` 106행 참고, 확인 필요) | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-05 | "저장" 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 코드 / `NO_INTERNET` / 알 수 없는 에러 | 스낵바 (NICKNAME-03과 동일 규칙) | `handleSaveFailure` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-06 | 비밀번호 표시 토글 | - | 공통 섹션 02와 동일 | - | - | - |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-EMAIL-07 | "저장" 버튼 (화면 간 자동 반영 확인) | EMAIL-02와 동일하게 저장 성공 | 새 이메일이 `userDataStore.saveEmail`을 거쳐 `email` Flow로 나가서, 재진입/새로고침 없이 설정 메인(`SettingMainScreen`) "이메일 변경" 행 표시값이 갱신된다. `email`은 `nickname`과 달리 홈 쪽에서 구독하는 곳이 없으니(코드베이스 전체에서 `userRepository.email` 구독자는 `SettingMainViewModel`뿐) 설정 메인 하나만 확인하면 된다 | - | - | 쓰는 곳: `UserRepositoryImpl.updateEmail` / 읽는 곳: `SettingMainViewModel.observeUser` |  |  |  |  |  |  |  |  |

## 비밀번호 변경 (`type = Password`)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGDETAIL-PASSWORD-01 | (자동) 새 비밀번호 입력란에 타이핑 | - | `isNewPasswordValid` 실시간 갱신 → "새 비밀번호" 라벨 옆 체크 아이콘(`AniPickValidationCheckIcon`)에 반영 | - | - | `observeNewPasswordValidation` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-02 | (자동) 새 비밀번호 확인란에 타이핑 | - | `isPasswordMatch` 실시간 갱신(새 비밀번호와 일치해야 true) → "새 비밀번호 확인" 라벨 옆 체크 아이콘에 반영 | - | - | `observePasswordMatch` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-03 | "저장" 버튼 | 현재 비밀번호 입력 + 새 비밀번호 유효 + 확인 일치(`isChangePasswordEnabled`) | 성공 시 `SaveSuccess` → 이전 화면으로 이동 | - | - | `SettingDetailViewModel.save` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-04 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 107(현재 비밀번호 불일치) | 인라인 에러 텍스트(`currentPasswordError`) — "현재 비밀번호가 일치하지 않습니다. 다시 입력해 주세요." | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-05 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 110(새 비밀번호 취약) | 인라인 에러 텍스트(`newPasswordError`) — "8~16자의 영문 대/소문자, 숫자, 특수문자를 조합하여 입력해 주세요." (클라이언트 실시간 검증을 통과했더라도 서버가 다시 거부할 수 있음) | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-06 | "저장" 버튼 | 위와 동일 | 위와 동일 | 서버 code 108(새 비밀번호 확인 불일치) | 인라인 에러 텍스트(`newPasswordConfirmError`) — "비밀번호가 일치하지 않습니다." | `applyFieldError` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-07 | "저장" 버튼 | 위와 동일 | 위와 동일 | 그 외 `Api` 코드 / `NO_INTERNET` / 알 수 없는 에러 | 스낵바 (NICKNAME-03과 동일 규칙) | `handleSaveFailure` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-PASSWORD-08 | 비밀번호 표시 토글 | - | 공통 섹션 02와 동일 — 현재/새/확인 3개 필드 모두 같이 토글됨 | - | - | - |  |  |  |  |  |  |  |  |

## 회원탈퇴 (`type = Withdrawal`)

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGDETAIL-WITHDRAWAL-01 | "탈퇴하기" 버튼 | - | 저장 로직 바로 실행 안 되고 확인 다이얼로그 먼저 표시("회원탈퇴 하시겠습니까?") | - | - | `SettingDetailViewModel.onAction` (`OnSaveClick`이 Withdrawal이면 `save()` 대신 다이얼로그부터) |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-WITHDRAWAL-02 | 탈퇴 다이얼로그 - "취소" 또는 바깥 탭 | 다이얼로그가 떠 있는 상태 | 다이얼로그만 닫힘, 탈퇴 안 됨 | - | - | `OnWithdrawDialogDismiss` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-WITHDRAWAL-03 | 탈퇴 다이얼로그 - "탈퇴하기" 확인 | 다이얼로그가 떠 있는 상태 | `userRepository.withdraw()` 성공 → 스낵바 "회원 탈퇴가 완료되었습니다." + `WithdrawSuccess` 이벤트 → `onWithdrawSuccess()`로 이동(로그인 화면 등, 상위 네비게이션이 결정) | - | - | `SettingDetailViewModel.save` (`type == Withdrawal`) |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGDETAIL-WITHDRAWAL-04 | 탈퇴 다이얼로그 - "탈퇴하기" 확인 | 위와 동일 | 위와 동일 | `userRepository.withdraw()` 실패 (코드 상관없이 전부 동일 처리 — 필드별 분기 없음) | 스낵바 — `Api` 에러면 서버 메시지 그대로, 아니면 "탈퇴하지 못했습니다. 잠시 후 다시 시도해주세요." | `SettingDetailViewModel.handleSaveFailure` (Withdrawal 전용 분기, `applyFieldError`를 안 탄다) |  |  |  |  |  |  |  |  |
