# Community - 게시글 작성 (CommunityWriteScreen)

패키지: `feature/community/impl/write`. 근거 파일: `CommunityWriteAction.kt`, `CommunityWriteState.kt`, `CommunityWriteViewModel.kt`, `CommunityWriteScreen.kt`, `components/CommunityWriteFields.kt`, `components/CommunityWritePhotoSection.kt`.

`postId`가 없으면 작성 모드(`createPost`), 있으면 수정 모드(`updatePost`) - `state.isEditMode`(`postId != null`)로 갈린다. 수정 모드는 진입 시 `loadExistingPost`로 기존 글을 불러와 채운다(첨부 이미지는 원격 URL이라 로컬 `Uri` 목록엔 안 채워짐 - 유지하려면 다시 첨부해야 함).

| ID | 이벤트/트리거 | 사전조건 | 예상 결과 | 실패 케이스 | 실패 시 UI | 근거 | 테스트 결과 | 특이사항 | 테스터 | 테스트 일자 | 앱 버전 | OS | OS 버전 | 기종 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| COMMUNITY-COMMUNITYWRITE-01 | 뒤로가기(상단 앱바) | - | 이전 화면으로 이동 | - | - | `CommunityWriteScreen` (Root에서 처리) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-02 | "등록"/"수정" 버튼(상단바 액션, `AniPickTopBarSubmitAction`) | 제목/내용 둘 다 입력됨(`isSubmitEnabled == true`) | 첨부 이미지를 순차 업로드(`uploadPostImage`, 있으면) → 작성 모드면 `createPost(seriesId, ...)`, 수정 모드면 `updatePost(postId, ...)` 호출 → 성공 시 스낵바("게시글이 등록되었습니다." 작성 모드 / "게시글이 수정되었습니다." 수정 모드) → `CommunityWriteEvent.WriteSuccess` → Root가 이전 화면으로 이동(`onWriteSuccess = navigator::goBack`) | - | - | `CommunityWriteViewModel.onSubmitClick` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-02b | "등록"/"수정" 버튼 | 제목 또는 내용 중 하나라도 비어있음(`isSubmitEnabled == false`) | 버튼 비활성화(회색) - 클릭 자체가 안 먹음 | - | - | `AniPickTopBarSubmitAction(enabled = state.isSubmitEnabled)` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-02c | "등록"/"수정" 버튼 | 첨부 이미지 1장 이상 | 위와 동일 | `uploadPostImage` 실패(이미지 바이트 읽기 실패 포함) | 스낵바("이미지를 불러오지 못했습니다." 또는 API 에러 메시지) + 제출 중단(`isSubmitting=false`) - 이미 업로드된 이미지가 있어도 게시글은 등록/수정되지 않는다 | `CommunityWriteViewModel.onSubmitClick`/`uploadImage` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-02d | "등록"/"수정" 버튼 | 이미지 업로드까지는 성공(또는 이미지 없음) | 위와 동일 | `createPost`/`updatePost` 실패 | 스낵바(API 에러 메시지) + `isSubmitting=false` - 화면 유지, 입력값은 그대로 남음 | `CommunityWriteViewModel.onSubmitClick` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-03 | "스포일러" 스위치 | - | `isSpoiler` 상태 토글 - 등록/수정 버튼을 눌러야 서버에 반영됨(로컬 상태만 우선 바뀜) | - | - | `OnSpoilerToggle` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-04 | 사진 추가 | 첨부된 이미지가 5장 미만 | 포토 피커로 이미지 1장 선택 → 목록에 추가 | - | - | `OnImageAdd` / `CommunityWriteViewModel.onImageAdd` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-04b | 사진 추가 | 이미 5장(`MAX_IMAGE_COUNT`) 첨부됨 | 추가 무시(`if (current.size >= MAX_IMAGE_COUNT) return`) - UI 안내 없이 조용히 막힘 | - | - | `CommunityWriteViewModel.onImageAdd` |  | 5장 제한에 도달했다는 안내 문구/토스트가 없어 사용자는 왜 안 추가되는지 알 수 없음 | | | | | | |
| COMMUNITY-COMMUNITYWRITE-05 | 첨부 이미지 삭제(x) | 첨부된 이미지 1장 이상 | 목록에서 해당 이미지 제거 | - | - | `OnImageRemove` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-06 | 제목 입력란에 텍스트 입력 | - | 텍스트 반영 + 제목/내용 둘 다 채워지면 등록 버튼 활성화(`observeSubmitEnabled`) | - | - | `CommunityWriteFields`(`titleState`) / `CommunityWriteViewModel.observeSubmitEnabled` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-07 | 내용 입력란에 텍스트 입력 | - | 텍스트 반영 + `0/1000`~`1000/1000` 글자 수 카운터 갱신, 1000자 초과 입력 불가(`maxLength`) | - | - | `CommunityWriteFields`(`contentState`, `CONTENT_MAX_LENGTH=1000`) |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-08 | (자동) 수정 모드 진입 시 기존 글 조회 | `postId != null` | `getPostDetail(postId)` 성공 → 제목/내용/스포일러 여부가 기존 글 값으로 채워짐(첨부 이미지는 안 채워짐) | - | - | `CommunityWriteViewModel.loadExistingPost` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-08b | (자동) 수정 모드 진입 | `postId != null` | 위와 동일 | `getPostDetail` 실패 | 스낵바(API 에러 메시지) + `state.error` 갱신(렌더링하는 곳은 없음) | `CommunityWriteViewModel.loadExistingPost` |  |  |  |  |  |  |  |  |
| COMMUNITY-COMMUNITYWRITE-09 | (화면 간 자동 반영 확인) 등록/수정 성공 | COMMUNITY-COMMUNITYWRITE-02 성공 | `communityRepository.refreshCommunityBoardPosts()` 호출 → 커뮤니티 메인 화면이 떠 있으면(가려져 있어도) 목록이 재조회돼 방금 등록/수정한 글이 반영됨 - 자세한 동작은 [`community-main.md`](./community-main.md)의 `COMMUNITY-COMMUNITYMAIN-10` 참고 | - | - | `CommunityWriteViewModel.onSubmitClick` |  |  |  |  |  |  |  |  |

## 알려진 미완성/이슈

- **`CommunityWriteState.titleError` 필드가 죽어있음**: 필드는 선언돼 있고 `CommunityWriteFields`에서 `state.titleError?.let { AniPickErrorText(it) }`로 렌더링까지 준비돼 있지만, `ViewModel` 어디서도 이 값을 설정하지 않는다 - 제목 유효성 검사 로직 자체가 없음.
- **이미지 5장 제한 도달 시 무음 실패 (COMMUNITY-COMMUNITYWRITE-04b)**: 안내 문구 없이 그냥 추가가 안 된다.
- **수정 모드에서 기존 첨부 이미지 표시 안 됨**: `loadExistingPost`가 텍스트만 채우고 `state.images`(로컬 `Uri` 목록)는 그대로 비워둔다 - 기존 이미지를 유지하려면 사용자가 다시 첨부해야 한다.
