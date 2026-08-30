# MyPage - 설정 메인 (SettingMainScreen)

패키지: `feature/mypage/impl/setting/main`. 근거 파일: `SettingMainAction.kt`, `SettingMainState.kt`, `SettingMainViewModel.kt`, `SettingMainScreen.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-SETTINGMAIN-01 | 뒤로가기(상단 앱바) | - | 이전 화면으로 이동 | - | - | `SettingMainScreen` (Root에서 처리) |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-02 | "닉네임 변경" 행 클릭 | - | 설정 상세 화면(닉네임 모드)으로 이동 | - | - | `SettingMainScreen`, `OnEditProfileClick(Nickname)` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-03 | "이메일 변경" 행 클릭 | `canEditCredentials == true` (provider == LOCAL, 즉 이메일 가입 계정) | 설정 상세 화면(이메일 모드)으로 이동 | - | - | `SettingMainScreen`, `OnEditProfileClick(Email)` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-03b | "이메일 변경" 행 | `canEditCredentials == false` (SNS 간편가입 계정) | 행이 비활성화되어 탭해도 반응 없음 | - | - | `SettingItemRow(isEnabled = ... && state.canEditCredentials)` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-04 | "비밀번호 변경" 행 클릭 | `canEditCredentials == true` | 설정 상세 화면(비밀번호 모드)으로 이동 | - | - | `SettingMainScreen`, `OnEditProfileClick(Password)` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-04b | "비밀번호 변경" 행 | `canEditCredentials == false` | 행이 비활성화되고, 우측에 "sns 간편가입된 계정입니다." 문구가 화살표 아이콘 대신 표시됨 | - | - | `SettingMainScreen:190-203` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-05 | "문의하기" 행 클릭 | - | 외부 브라우저로 문의 폼(Google Form) 열림 | - | - | `SettingMainScreen`, `OnContactClick` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-06 | "서비스 이용약관" 행 클릭 | - | 외부 브라우저로 약관 페이지 열림 | - | - | `OnTermsClick` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-07 | "개인정보 처리방침" 행 클릭 | - | 외부 브라우저로 개인정보처리방침 페이지 열림 | - | - | `OnPrivacyPolicyClick` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-08 | "오픈소스 라이선스" 행 클릭 | - | `OssLicensesMenuActivity` 화면으로 이동 | - | - | `OnOpenSourceLicenseClick` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-09 | "공지사항" 행 클릭 | - | 외부 브라우저로 공지사항(Notion) 페이지 열림 | - | - | `OnNoticeClick` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-10 | "로그아웃" 행 클릭 | - | 로그아웃 확인 다이얼로그 표시("로그아웃 하시겠습니까?") | - | - | `OnLogoutClick`, `showLogoutDialog` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-11 | 로그아웃 다이얼로그 - "로그아웃" 확인 | 다이얼로그가 떠 있는 상태 | 로컬 데이터 삭제(`authRepository.clearLocalData`) 후 `LogoutConfirmed` 이벤트 → 로그인 화면 등으로 이동(`onLogout`) | (API 호출 없음 — 로컬 클리어만이라 실패 케이스 없음) | - | `SettingMainViewModel.logout` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-12 | 로그아웃 다이얼로그 - "취소" 또는 바깥 영역 탭 | 다이얼로그가 떠 있는 상태 | 다이얼로그만 닫힘, 로그아웃 안 됨 | - | - | `OnLogoutDialogDismiss` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-13 | "회원탈퇴" 행 클릭 | - | 설정 상세 화면(회원탈퇴 모드)으로 이동 | - | - | `OnEditProfileClick(Withdrawal)` — Action은 02~04와 동일하지만 트리거 위치가 "기타" 섹션이라 별도 행으로 분리 |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-14 | (자동) 화면 진입 시 사용자 설정 로드 | - | `provider` 등 로드되어 위 행들의 활성화 상태 반영, 로드 중엔 각 행 우측에 셰이머 표시 | `userRepository.getUserSetting()` 실패 | 스낵바 "설정 정보를 불러오지 못했습니다" | `SettingMainViewModel.loadUserSetting` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-15 | (표시 확인) "연동 SNS" 행 | - | 클릭 불가(항상 비활성화), 우측에 `provider`에 맞는 SNS 라벨 텍스트 표시(`AuthProvider.snsLabel()`) | - | - | `SettingMainScreen:208-223` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-16 | (표시 확인) "앱 버전" 행 | - | 클릭 불가, 우측에 `BuildConfig.APP_VERSION` 표시 | - | - | `SettingMainScreen:237-247` |  |  |  |  |  |  |  |  |
| MYPAGE-SETTINGMAIN-17 | (화면 간 자동 반영 확인) 이 화면이 떠 있는 상태에서 설정 상세(닉네임/이메일 변경)로 이동해 저장 성공 | 이 화면(설정 메인)이 백스택에 살아있는 상태에서 설정 상세로 진입 | 설정 상세에서 저장에 성공하고 뒤로가기로 돌아오면, "닉네임 변경"/"이메일 변경" 행의 표시값이 별도 재조회 없이 새 값으로 보인다 — `nickname`/`email`이 `combine`으로 구독 중이라 `SettingMainViewModel` 인스턴스가 그대로 유지된다면 뒤로가기 전에 이미 최신값으로 갱신돼 있어야 정상 | `userRepository.nickname`/`.email` 갱신이 실제로 반영 안 됨(구독이 끊기거나 최초 1회만 읽는 등) | (버그) 돌아왔는데도 이전 값이 그대로 보임 | 읽는 곳: `SettingMainViewModel.observeUser`(02/03/04 행 표시값의 소스) — 쓰는 곳/전체 구독자 목록은 `setting-detail.md`의 `MYPAGE-SETTINGDETAIL-NICKNAME-04`/`-EMAIL-07` 참고 |  |  |  |  |  |  |  |  |
