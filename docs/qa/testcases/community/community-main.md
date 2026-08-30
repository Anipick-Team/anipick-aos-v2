# Community - 커뮤니티 메인 (CommunityMainScreen)

패키지: `feature/community/impl/main`. 근거 파일: `CommunityMainAction.kt`, `CommunityMainState.kt`, `CommunityMainViewModel.kt`, `CommunityMainScreen.kt`, `components/CommunityAnimeInfoSection.kt`, `components/CommunityFilterSection.kt`, `components/CommunityPostItem.kt`. 게시글 목록 캐시는 `core/data/community/CommunityRepository.kt`(`communityBoardPosts`).

상단 애니 정보(제목/커버/장르)는 진입 시점에 넘겨받은 `CommunityBoard` 값을 그대로 쓰고 이 화면에서 재조회하지 않는다.

**게시글 목록은 이 화면이 직접 들고 있지 않고 `CommunityRepository.communityBoardPosts`(싱글턴 캐시)를 구독한다.** `CommunityMainViewModel`은 진입 시 `loadCommunityBoardPosts`를 한 번 호출해 캐시를 채우고, 이후엔 그 캐시를 그냥 구독만 한다 - 글쓰기 화면에서 등록/수정에 성공하거나 게시글 상세에서 삭제에 성공하면 그쪽이 `refreshCommunityBoardPosts()`를 불러 캐시를 갱신하고, 이 화면은 재진입/라이프사이클 감지 없이 구독 중인 `Flow`로 자동 반영된다(좋아요/댓글은 캐시를 안 건드리므로 재조회 트리거가 아니다). 이 화면을 완전히 벗어나면(`CommunityMainViewModel.onCleared`) 캐시가 비워져서, 다른 게시판에 새로 들어가면 항상 스켈레톤 → 빈 목록부터 다시 시작한다.

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| COMMUNITY-COMMUNITYMAIN-01 | 뒤로가기(상단 앱바) | - | 이전 화면으로 이동 | - | - | `CommunityMainScreen` (Root에서 처리) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-02 | 글쓰기(FAB) | - | 게시글 작성 화면으로 이동 | - | - | `OnWriteClick` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-03 | 게시글 카드 클릭 | `state.posts`에 항목 있음 | 게시글 상세 화면으로 이동 | - | - | `OnPostClick` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-04 | 게시글 정렬 필터 칩(전체/월간인기/주간인기/일간인기) | 현재 선택된 필터가 클릭한 필터가 아님 | 선택한 필터로 첫 페이지부터 재조회(`sortParam`: latest/popularMonthly/popularWeekly/popularDaily) - 재조회 중엔 기존 목록 대신 스켈레톤(`CommunityPostItemSkeleton` 4개) 표시, 성공하면 새 목록으로 교체 | 재조회 실패 | 스켈레톤이 사라지고 재조회 이전 목록으로 그대로 복귀(레포지토리 캐시가 실패 시 `posts`를 안 건드림) + 스낵바(API 에러 메시지) | `OnFilterClick` / `CommunityMainViewModel.observeBoardPosts` / `CommunityMainScreen`(`isPostsLoading` 분기) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-05 | "스포일러 보기" 스위치 | - | `isSpoilerVisible` 상태 토글(기본값 켜짐) - 재조회 없이 `state.visiblePosts`가 즉시 갱신됨(꺼면 `post.isSpoiler == true`인 게시글이 클라이언트에서 목록/스켈레톤 카운트와 무관하게 걸러짐), 스포일러 게시글만 있던 경우 빈 목록 문구가 "스포일러 게시글만 있어요.\n스포일러 노출을 켜보세요."로 바뀜 | - | - | `OnSpoilerVisibleChange` / `CommunityMainState.visiblePosts` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-06 | 목록 스크롤 중 하단 도달(무한 스크롤) | 마지막 페이지 아님 + 로딩 중 아님 | 다음 페이지를 이어붙임 | 추가 로드 실패 | 목록은 그대로 유지(이어붙이기 실패, 기존 페이지는 안 사라짐) + 스낵바(API 에러 메시지) | `OnLoadMorePosts` / `CommunityMainViewModel.loadMorePosts`/`observeBoardPosts` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-07 | (자동) 화면 진입 시 게시글 목록 조회 | `seriesId != null` | 로딩 중엔 게시글 카드 자리에 스켈레톤(`CommunityPostItemSkeleton`, 4개) 표시 → `getCommunityPosts(seriesId, sort=postFilter.sortParam)` 성공하면 애니 정보 + 필터 섹션 + 게시글 카드 목록으로 교체 | - | - | `CommunityMainViewModel.loadPosts` / `CommunityMainScreen`(`isPostsLoading` 분기) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-07b | (자동) 화면 진입 | `seriesId != null` | 위와 동일 | `getCommunityPosts` 실패(네트워크/Api/알 수 없음 무관) | 스켈레톤이 사라지고 `AniPickEmptyState`(에러 메시지 + 재시도 버튼)로 대체 - 스낵바/인라인 텍스트는 아님 | `CommunityMainViewModel.loadPosts` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-08 | (자동, 결과 없음) 게시글 0건 | 조회 성공했지만 `posts`가 빈 리스트, 로딩 중 아님 | 애니 정보 + 필터 섹션 아래 "아직 작성된 게시글이 없어요.\n첫 글을 남겨보세요." 안내(`AniPickEmptyState`) 표시 | - | - | `CommunityMainScreen`(`!state.isPostsLoading && state.visiblePosts.isEmpty()` 분기) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-09 | (표시 확인) 진입 경로가 애니 정보를 안 들고 온 경우 | `title == null` | 커버/제목/장르 섹션은 생략되고 필터 섹션만 표시 | - | - | `CommunityAnimeInfoSection`(`title != null` 분기) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-10 | (화면 간 자동 반영 확인) 글쓰기 화면에서 게시글 등록/수정 성공 | 이 화면이 떠 있는 상태(전면이든 Write에 가려져 있든) | `CommunityWriteViewModel.onSubmitClick` 성공 시 `communityRepository.refreshCommunityBoardPosts()` 호출 → 캐시가 첫 페이지부터 재조회되고, 이 화면은 `communityBoardPosts`를 구독 중이라 재진입 없이 목록이 갱신됨(글쓰기 화면에 가려져 있었으면 뒤로가기로 돌아왔을 때 이미 최신 상태) - 이때도 재조회 중엔 스켈레톤이 뜬다(COMMUNITY-COMMUNITYMAIN-04와 동일 메커니즘) | 재조회 실패 | 기존 목록(캐시가 안 건드림) 유지 + 스낵바(API 에러 메시지) - 화면이 안 보이는 중이었으면 다음에 봤을 때 이미 그 상태 | `CommunityMainViewModel.observeBoardPosts` / `CommunityRepositoryImpl.refreshCommunityBoardPosts` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-11 | (화면 간 자동 반영 확인) 게시글 상세에서 삭제 성공 | 위와 동일 | `CommunityDetailViewModel.onDeleteConfirm` 성공 시에도 같은 방식으로 캐시 재조회 → 삭제된 게시글이 목록에서 사라짐 | 재조회 실패 | 위와 동일 | `CommunityDetailViewModel.onDeleteConfirm` / `CommunityRepositoryImpl.refreshCommunityBoardPosts` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-12 | (화면 간 자동 반영 확인 - 반영 안 됨) 게시글/댓글 좋아요 | 게시글 상세에서 좋아요 토글 | 캐시를 안 건드리므로 이 화면 목록의 좋아요 숫자는 그대로 - 다시 조회해야(예: 다른 게시판 들어갔다 재진입) 반영됨 | - | - | 요청 범위(좋아요/댓글은 재조회 트리거 아님) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYMAIN-13 | (자동) 화면(ViewModel)이 완전히 사라질 때 캐시 정리 | 뒤로가기 등으로 `CommunityMainViewModel`이 clear됨(Write/Detail로 갔다가 돌아온 것이 아니라 이 화면 자체를 벗어남) | `communityRepository.clearCommunityBoardPosts()` 호출 → 캐시가 초기값으로 리셋. 다른 게시판에 새로 들어가면 `seriesId`가 달라도 항상 스켈레톤부터(이전 게시판 목록이 잠깐이라도 안 비침) | - | - | `CommunityMainViewModel.onCleared` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **스포일러 게시글도 제목/본문은 그대로 노출됨**: 스위치를 꺼도 목록에서 스포일러 게시글 자체를 빼줄 뿐(`visiblePosts`), 노출 상태에서(스위치 켬) 보이는 개별 게시글의 제목/본문을 가리는 기능(블러/모자이크 등)은 없다 - `CommunityPostItem`은 `post.isSpoiler == true`면 "스포일러" 태그만 붙인다. 게시글 상세(`CommunityDetailHeader`)도 동일.
- **`seriesId == null`이면 목록 API 자체가 호출되지 않음** - 이 경로로 진입할 일이 실제로 있는지, 있다면 사용자에게 어떤 안내가 필요한지 확인 필요.
