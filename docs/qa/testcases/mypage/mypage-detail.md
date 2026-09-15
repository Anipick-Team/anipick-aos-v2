# MyPage - 마이페이지 디테일 (MyPageDetailScreen)

패키지: `feature/mypage/impl/detail`. 근거 파일: `MyPageDetailAction.kt`, `MyPageDetailState.kt`, `MyPageDetailViewModel.kt`, `MyPageDetailScreen.kt`, `components/*.kt`, 타입 정의 `feature/mypage/api/.../MyPageDetailType.kt`. 에러 메시지는 [`error-codes.md`](../../reference/error-codes.md) 참고.

`type`(`MyPageDetailType`) 값에 따라 화면 레이아웃이 완전히 갈린다 — WatchList/Watching/Finished/LikedAnimes는 `MyPageAnimeGrid`(3열 고정 그리드), LikedPersons는 `MyPagePersonGrid`(3열 고정, 인물), MyContent는 탭(내 게시글/내 댓글)이 있는 리스트. 이벤트/트리거 칸에 해당 타입을 명시했다.

**RatedAnimes(평가한 작품)는 `feature/review/impl/rated`로 이전됨** - `MyPageDetailType`에서도 케이스가 빠졌다. 아래 04/05/14/14b~d 행은 번호를 재사용하지 않기 위해 "이동됨"으로만 표시하고, 실제 케이스는 [`review/review-rated.md`](../review/review-rated.md)를 본다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| MYPAGE-MYPAGEDETAIL-01 | 뒤로가기(상단 앱바) | - | 이전 화면으로 이동 | - | - | `MyPageDetailScreen` (Root에서 처리) |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-02 | 애니 카드 클릭 | type이 WatchList/Watching/Finished/LikedAnimes 중 하나 | 애니 상세 화면으로 이동 | - | - | `OnAnimeClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-03 | 인물 카드 클릭 | type == LikedPersons | 인물 상세 화면으로 이동 | - | - | `OnPersonClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-04 | (이동됨) 정렬 드롭다운 | - | `review/review-rated.md`의 `REVIEW-RATEDREVIEW-04` 참고 | - | - | - |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-05 | (이동됨) "리뷰만 보기" 스위치 | - | `review/review-rated.md`의 `REVIEW-RATEDREVIEW-05` 참고 | - | - | - |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-06 | "내 게시글" / "내 댓글" 탭 전환 | type == MyContent | 해당 탭으로 전환 + 목록/커서 초기화 후 첫 페이지부터 재조회 | - | - | `OnMyContentTabSelected` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-07 | 게시글 카드 클릭 | type == MyContent, "내 게시글" 탭 | 커뮤니티 게시글 상세 화면으로 이동 | - | - | `OnPostClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-08 | 댓글 카드 클릭 | type == MyContent, "내 댓글" 탭 | 해당 댓글이 달린 게시글 상세 화면으로 이동(`comment.postId` 기준) | - | - | `OnCommentClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-09 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙이고 하단에 로딩 인디케이터 표시 | 추가 로드 실패 | - (목록이 이미 안 비어있어 화면엔 안 뜸 - 로딩 인디케이터만 사라지고 기존 목록 유지) | `OnLoadMore` / `MyPageDetailViewModel.loadMore` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-10 | 다시 시도 버튼 | 목록 영역이 에러로 인한 빈 상태(재시도 버튼 노출됨) | 현재 필터/정렬/탭 기준으로 첫 페이지부터 재조회 | - | - | `OnRetryClick` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-11 | (자동) 화면 진입 - WatchList/Watching/Finished 목록 조회 | type이 해당 3종 중 하나 | `getMyPageAnimes(status)` 성공 → "총 n개" 헤더 + 3열 그리드 표시(로딩 중엔 스켈레톤). Finished는 카드마다 내 평점 설명(`MyRatingDescription`: 평점 없으면 "아직 평가가 없어요", 있으면 "내 평가 ★ x.x") 추가 표시 | - | - | `MyPageDetailViewModel.loadAnimesByStatus` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-11b | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 네트워크 연결 없음 | 목록 영역에 "네트워크 연결을 확인해주세요." + "다시 시도" 버튼(`AniPickEmptyState`) | `MyPageDetailViewModel.applyFailure` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-11c | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 그 외 `Api` 에러 | 서버 에러 메시지(없으면 "알 수 없는 오류가 발생했습니다.") + "다시 시도" 버튼 | `MyPageDetailViewModel.applyFailure` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-11d | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 그 외 알 수 없는 에러 | "알 수 없는 오류가 발생했습니다." + "다시 시도" 버튼 | `MyPageDetailViewModel.applyFailure` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-12 | (자동) 화면 진입 - LikedAnimes 목록 조회 | type == LikedAnimes | `getLikedAnimes()` 성공 → "총 n개" 헤더 + 3열 그리드 표시 | - | - | `MyPageDetailViewModel.loadLikedAnimes` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-12b~d | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 네트워크 없음 / 그 외 Api / 알 수 없음 | MYPAGE-MYPAGEDETAIL-11b/c/d와 동일 패턴 | `MyPageDetailViewModel.loadLikedAnimes` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-13 | (자동) 화면 진입 - LikedPersons 목록 조회 | type == LikedPersons | `getLikedPersons()` 성공 → "총 n명" 헤더 + 3열 그리드(인물) 표시 | - | - | `MyPageDetailViewModel.loadLikedPersons` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-13b~d | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 네트워크 없음 / 그 외 Api / 알 수 없음 | MYPAGE-MYPAGEDETAIL-11b/c/d와 동일 패턴 | `MyPageDetailViewModel.loadLikedPersons` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-14 | (이동됨) 화면 진입 - 평가한 작품 목록 조회 | - | `review/review-rated.md`의 `REVIEW-RATEDREVIEW-11` 참고 | - | - | - |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-14b~d | (이동됨) 화면 진입 실패 케이스 | - | `review/review-rated.md`의 `REVIEW-RATEDREVIEW-11b~d` 참고 | - | - | - |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-15 | (자동) 화면 진입 - MyContent(내 게시글) 목록 조회 | type == MyContent, 기본 탭 POSTS | `getMyCommunityPosts()` 성공 → 탭 + "총 n개" + 게시글 카드 목록(`MyPagePostItem`) 표시 | - | - | `MyPageDetailViewModel.loadMyPosts` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-15b~d | (자동) 화면 진입 | 위와 동일 | 위와 동일 | 네트워크 없음 / 그 외 Api / 알 수 없음 | MYPAGE-MYPAGEDETAIL-11b/c/d와 동일 패턴 | `MyPageDetailViewModel.loadMyPosts` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-16 | (자동) 탭 전환 후 - MyContent(내 댓글) 목록 조회 | type == MyContent, COMMENTS 탭 선택 | `getMyCommunityComments()` 성공 → 댓글 카드 목록(`MyPageCommentItem`) 표시 | - | - | `MyPageDetailViewModel.loadMyComments` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-16b~d | (자동) 탭 전환 후 | 위와 동일 | 위와 동일 | 네트워크 없음 / 그 외 Api / 알 수 없음 | MYPAGE-MYPAGEDETAIL-11b/c/d와 동일 패턴 | `MyPageDetailViewModel.loadMyComments` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-17 | (자동, 결과 없음) 각 type별 빈 목록 | 조회 성공했지만 항목 0개 | type별 안내 문구 표시 - WatchList "아직 볼 애니가 없어요.", Watching "아직 보는 중인 애니가 없어요.", Finished "아직 다 본 애니가 없어요.", LikedAnimes/LikedPersons "아직 좋아요한 작품/인물이 없어요.\n좋아요를 누르러 가볼까요 ?", MyContent "아직 작성한 게시글/댓글이 없어요." (RatedAnimes 빈 목록은 `review/review-rated.md`의 `REVIEW-RATEDREVIEW-12` 참고) | - | - | `MyPageDetailType.emptyMessage()` / `MyContentTabContent` |  |  |  |  |  |  |  |  |
| MYPAGE-MYPAGEDETAIL-18 | (화면 간 자동 반영 확인) 이 화면이 떠 있는 상태에서 애니/성우 상세에서 찜 토글, 또는 애니 상세에서 시청 상태 변경 | type이 WatchList/Watching/Finished/LikedAnimes/LikedPersons 중 하나(MyContent 제외)로 이 화면이 백스택에 살아있는 상태에서 카탈로그 애니/성우 상세로 이동 | 재진입 없이 1페이지부터 자동 재조회되어, 해당 목록에서 항목이 추가/제거되거나 순서가 바뀐다 | `userRepository.myPageProfile` 갱신이 반영 안 됨(구독이 끊기는 등) | (버그) 돌아왔는데도 목록이 갱신 안 됨 | 읽는 곳: `MyPageDetailViewModel.observeMyPageProfileChanges` — 쓰는 곳은 [`catalog/catalog-anime.md`](../catalog/catalog-anime.md)의 CATALOG-CATALOGANIME-03/07, [`catalog/catalog-actor.md`](../catalog/catalog-actor.md)의 CATALOG-CATALOGACTOR-05 참고 |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **RatedAnimes(평가한 작품) 관련 이슈는 `review/review-rated.md`로 이전됨** - 정렬 API 값 미검증, "리뷰만 보기" 필터의 스크롤 트레이드오프 등.
