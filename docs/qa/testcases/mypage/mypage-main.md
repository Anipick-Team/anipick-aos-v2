# MyPage - 마이페이지 메인 (MyPageMainScreen)

패키지: `feature/mypage/impl/main`. 근거 파일: `MyPageMainAction.kt`, `MyPageMainState.kt`, `MyPageMainViewModel.kt`, `MyPageMainScreen.kt`, `components/*.kt`.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-MYPAGEMAIN-01 | "볼 애니" 박스 | - | 마이페이지 디테일(WatchList) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.WatchList)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-01b | "보는 중" 박스 | - | 마이페이지 디테일(Watching) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.Watching)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-01c | "다 본 애니" 박스 | - | 마이페이지 디테일(Finished) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.Finished)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-02 | 좋아요한 작품 카드 클릭 | `state.likedAnimes`에 항목 있음 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-03 | 좋아요한 인물 카드 클릭 | `state.likedPersons`에 항목 있음 | 인물 상세 화면으로 이동 | - | - | `OnPersonClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-04 | 설정 아이콘(상단바) | - | 설정 메인 화면으로 이동 | - | - | `OnSettingClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-05 | 프로필 이미지 탭 → 포토 피커에서 이미지 1장 선택 | - | 이미지 업로드 성공 시 프로필 이미지 즉시 갱신(기존 URL 있으면 imageId만 치환, 없으면 `loadMyPage()` 재조회) | `userRepository.updateProfileImage` 실패 | UI 반응 없음(성공 콜백 안에서만 상태 갱신 - `onFailure` 처리 자체가 없어 실패해도 아무 피드백 없음) | `MyPageMainViewModel.changeProfileImage` |  |  |  |  | | | | | |
| MYPAGE-MYPAGEMAIN-06 | "평가한 작품" 헤더 더보기 | - | 평가한 작품 화면(review 모듈 `ReviewNavKey.Rated`)으로 이동 - 상세 케이스는 [`review/review-rated.md`](../review/review-rated.md) | - | - | `OnRatedAnimesClick` (Root에서 처리) |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-07 | "내 콘텐츠" 헤더 더보기 | - | 마이페이지 디테일(MyContent) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.MyContent)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-08 | "좋아요한 작품" 섹션 더보기 | - | 마이페이지 디테일(LikedAnimes) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.LikedAnimes)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-08b | "좋아요한 작품" 섹션 더보기 아이콘 | `state.likedAnimes`가 빈 리스트 | 더보기 버튼 비활성화(`enabled = hasContent`), 대신 "아직 좋아요한 작품이 없어요.\n좋아요를 누르러 가볼까요 ?" 안내 문구 표시 | - | - | `MyPageListSection`(`AniPickSectionHeader(enabled = hasContent)` / `EmptyItems`) |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-09 | "좋아요한 인물" 섹션 더보기 | - | 마이페이지 디테일(LikedPersons) 화면으로 이동 | - | - | `OnDetailClick(MyPageDetailType.LikedPersons)` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-09b | "좋아요한 인물" 섹션 더보기 아이콘 | `state.likedPersons`가 빈 리스트 | 더보기 버튼 비활성화, "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요 ?" 안내 문구 표시 | - | - | `MyPageListSection` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-10 | (자동) 화면 진입 시 프로필/카운트/좋아요 목록 로드 | - | `getMyPage()` 성공 → 닉네임/프로필 이미지/시청 상태별 카운트(볼 애니·보는 중·다 본 애니)/좋아요한 작품·인물 목록 표시 | `userRepository.getMyPage()` 실패 | UI 반응 없음 - `state.error`에 저장되지만 `MyPageMainScreen`이 이 값을 어디서도 렌더링하지 않음(스낵바/인라인 텍스트 없음), `state.isLoading`도 화면에서 안 읽어서 로딩 중에도 스켈레톤 없이 빈 값(0개/빈 리스트)으로만 보임 | `MyPageMainViewModel.loadMyPage` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEMAIN-11 | "피드백 보내기" 카드(상단) | - | 미구현 - 아무 동작 없음(`onFeedbackClick = { }` 하드코딩) | - | - | `myPageMainSections` → `FeedbackLinkCard` |  | 클릭 핸들러 자체가 빈 람다라 어디로도 연결되지 않음 | | | | | | |

## 알려진 미완성/이슈

- **화면 진입 실패 시 사용자 피드백 없음 (MYPAGE-MYPAGEMAIN-10)**: `MyPageMainViewModel.loadMyPage()`가 실패해도 `state.error`만 채우고 `MyPageMainScreen`은 그 값을 전혀 사용하지 않는다. 다른 화면들(Ranking/Search/Home 등)에서 이미 적용된 `AniPickEmptyState` + 재시도 패턴이 여기엔 없다. `state.isLoading`도 마찬가지로 화면에서 읽지 않아 스켈레톤이 없다.
- **프로필 이미지 업로드 실패 시 무반응 (MYPAGE-MYPAGEMAIN-05)**: `changeProfileImage`가 `.onSuccess`만 있고 `.onFailure`가 없어 업로드 실패 시 아무 것도 일어나지 않는다(로딩 인디케이터도 없어 사용자는 실패했는지조차 알 수 없음).
- **"피드백 보내기" 카드 미연결 (MYPAGE-MYPAGEMAIN-11)**: `onFeedbackClick = { }`로 하드코딩되어 있어 클릭해도 아무 반응이 없다.
- **닉네임 미표시**: `MyPageMainState.nickname`을 로드하긴 하지만 `MyPageTopAppBar`/`myPageMainSections` 어디에도 렌더링하는 곳이 없다 - 죽은 필드로 보인다.
