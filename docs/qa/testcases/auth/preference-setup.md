# Auth - 취향 입력 (PreferenceSetupScreen)

패키지: `feature/auth/impl/preferencesetup`. 근거 파일: `PreferenceSetupAction.kt`, `PreferenceSetupState.kt`, `PreferenceSetupEvent.kt`, `PreferenceSetupViewModel.kt`, `PreferenceSetupScreen.kt`, `preferencesetup/components/PreferenceSetupHeader.kt`, `PreferenceSetupSearchFilterSection.kt`, `PreferenceSetupAnimeList.kt`, `PreferenceSetupAnimeItem.kt`, `PreferenceSetupCompleteBar.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| AUTH-PREFERENCESETUP-01 | 건너뛰기 | - | 빈 평가 목록으로 제출(`reviewRepository.submitReviews(emptyList())`) 후 홈으로 이동 | - | - | `PreferenceSetupViewModel.skip` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-01b | 건너뛰기 | - | 위와 동일(제출 실패와 무관하게 홈으로는 이동함) | 제출 API 실패 | `NO_INTERNET` → 스낵바 "네트워크 연결을 확인해주세요." / 그 외 → 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") — 스낵바는 전역 큐라 홈 화면으로 넘어간 뒤에도 표시됨 | `PreferenceSetupViewModel.handleSubmitFailure` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-02 | 검색 버튼(돋보기) | - | 검색어 + 현재 필터(년도·분기·장르) 기준으로 목록 재조회(첫 페이지부터) | 검색 API 실패 | 목록 영역에 "네트워크 연결을 확인해주세요." 빈 상태 표시(`AniPickEmptyState`) — 네트워크/서버 에러 구분 없이 항상 같은 문구 | `PreferenceSetupViewModel.searchAnimes` / `PreferenceSetupAnimeList` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-03 | 검색어 지우기(X 아이콘) | 검색어 입력됨 | 검색어 입력란만 초기화 — 목록은 즉시 재조회되지 않고, 다시 검색 버튼을 눌러야 반영됨 | - | - | `PreferenceSetupViewModel.onAction`(`OnSearchClearClick`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-04 | 목록 스크롤 중 하단 3번째 아이템 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고, 하단에 로딩 인디케이터 표시 | 추가 로드 API 실패 | - (에러 로그만 남기고 아무 표시도 안 함 — 로딩 인디케이터만 사라지고 기존 목록 유지) | `PreferenceSetupViewModel.searchAnimes(resetCursor=false)` / `PreferenceSetupScreen`(`LoadMoreEffect`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-05 | 필터 칩(년도/분기/장르) | - | 해당 필터 바텀시트 열림(`activeFilterSheet`) | - | - | `PreferenceSetupViewModel.onAction`(`OnFilterChipClick`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-06 | 필터 바텀시트 닫기(바깥 탭 등) | 필터 바텀시트 열려있음 | 바텀시트만 닫힘(`activeFilterSheet = null`), 필터 선택값은 변경되지 않음 | - | - | `PreferenceSetupViewModel.onAction`(`OnFilterSheetDismiss`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-07 | 필터 바텀시트 "적용" | 필터 바텀시트 열려있음 | 선택한 년도/분기/장르로 목록 재조회(첫 페이지부터) + 바텀시트 닫힘 | 검색 API 실패 | AUTH-PREFERENCESETUP-02와 동일 | `PreferenceSetupViewModel.onAction`(`OnAnimeFilterConfirm`) / `searchAnimes` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-08 | 애니메이션 카드 펼쳐서 "평가하기" | - | 별점 저장(로컬 상태, API 호출 없음) + "진행한 평가 수" 증가 + 완료 버튼 활성화 조건 충족 가능 | - | - | `PreferenceSetupViewModel.onAction`(`OnSaveRatingClick`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-09 | 애니메이션 카드 "평가취소" | 해당 작품 평가 완료 상태 | 별점 삭제(로컬 상태) + "진행한 평가 수" 감소 | - | - | `PreferenceSetupViewModel.onAction`(`OnCancelRatingClick`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-10 | 완료 버튼 | 평가한 작품 1개 이상 (버튼 활성 조건, `isCompleteEnabled`) | 평가 목록 제출(`reviewRepository.submitReviews`) 성공 → 홈으로 이동 | - | - | `PreferenceSetupViewModel.complete` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-10b | 완료 버튼 | 위와 동일 | 위와 동일 | 제출 API 실패 | `NO_INTERNET` → 스낵바 "네트워크 연결을 확인해주세요." / 그 외 → 스낵바(서버 에러 메시지, 없으면 "알 수 없는 오류가 발생했습니다.") | `PreferenceSetupViewModel.handleSubmitFailure` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-11 | 화면 진입(자동) | - | 필터 메타데이터(선택 가능한 년도/분기/장르 목록) 조회 | - | - | `PreferenceSetupViewModel.fetchMetadata` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-11b | 화면 진입(자동) | - | 위와 동일 | 메타데이터 조회 실패 — 네트워크 연결 없음 | 스낵바 "네트워크 연결을 확인해주세요." (지속 UI 없음) | `PreferenceSetupViewModel.handleMetadataFailure` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-11c | 화면 진입(자동) | - | 위와 동일 | 메타데이터 조회 실패 — 그 외(서버 통신 문제) | 필터 바텀시트를 열면 시트 콘텐츠 영역에 "필터 정보를 불러오지 못했습니다." + "다시 시도" 버튼 표시(`isMetadataError`, `AniPickAnimeFilterBottomSheet`) | `PreferenceSetupViewModel.handleMetadataFailure` / `PreferenceSetupScreen` |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-11d | 필터 바텀시트 내 "다시 시도" 버튼 | AUTH-PREFERENCESETUP-11c 상태(필터 바텀시트가 재시도 UI를 보여줌) | 메타데이터 재조회 — 성공 시 바텀시트에 정상 필터 콘텐츠 표시(AUTH-PREFERENCESETUP-11과 동일 분기 재적용) | - | - | `PreferenceSetupViewModel.onAction`(`OnMetadataRetryClick`) |  |  |  |  |  |  |  |  |
| AUTH-PREFERENCESETUP-12 | 화면 진입(자동) | - | 애니메이션 목록 최초 조회, 로딩 중엔 스켈레톤 8개 표시 | 최초 조회 실패 | AUTH-PREFERENCESETUP-02와 동일 | `PreferenceSetupViewModel.init` / `searchAnimes` |  |  |  |  |  |  |  |  |

## 개발 누락/참고 사항

- **`OnAnimeFilterConfirm.type`은 이 화면에서 항상 버려진다.** `FilterType` enum엔 `TYPE`(작품 형식: TV/극장판 등)이 있고 액션도 `type: String?`을 받지만, `PreferenceSetupScreen`이 `AniPickAnimeFilterBottomSheet`를 `showTypeTab = false`로 렌더링해서 애초에 사용자가 type을 고를 수 없고, `PreferenceSetupState`에도 `selectedType` 필드가 없어 `PreferenceSetupViewModel.onAction`이 `action.type`을 그냥 무시한다. `AnimeRepository.searchPreferenceSetupAnimes`도 type 파라미터가 없다 — 확인해보니 처음부터 이 화면 스코프에서 type 필터를 의도적으로 뺀 것으로 보인다(연도/분기/장르만 노출). 버그는 아니지만 "왜 파라미터가 있는데 안 쓰이지"에 대한 설명으로 남겨둔다.
