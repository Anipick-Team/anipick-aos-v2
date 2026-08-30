# DTO / Model Nullable 컨벤션

`core/network`의 응답 DTO, `core/model`의 도메인 모델, 그리고 그 사이를 잇는 `toXxx()` 매퍼에 대한 nullable 처리 규칙. 새 엔드포인트/DTO/모델을 추가할 때마다 필드별로 다시 판단하지 말고 아래 규칙을 그대로 적용한다.

## 왜 이렇게 하나

백엔드는 값이 없는 필드에 대해 키를 생략하지 않고 JSON `null`을 명시적으로 내려준다. kotlinx.serialization은 키가 **없을 때만** Kotlin 쪽 기본값을 적용하고, 키가 **`null`로 명시돼 있을 때는** 기본값을 적용하지 않는다.

그래서 예전처럼 모델 필드를 `= ""`, `= 0` 같은 fake 기본값으로 non-null 처리해두면, 실제로 그 필드가 `null`로 내려오는 응답에서 역직렬화가 그대로 터진다. `CatalogAnimeScreen`의 "작품 정보" 탭에서 실제로 겪은 크래시가 이 케이스였다.

**`coerceInputValues = true`는 의도적으로 켜지 않는다.** 켜면 크래시는 막히지만 `null`이 조용히 `""`/`0`/`false`로 둔갑해서, "값이 없다"와 "값이 빈 문자열이다"를 구분할 수 없게 된다. 그러면 아래 4번(null을 어떻게 보여줄지는 화면이 정한다)이 통째로 무너진다 — 화면은 이미 뭉개진 값을 받게 되고, 섹션을 숨겨야 할 자리에 빈 칸이 그려진다. 크래시를 막는 방법으로 nullable을 고른 것이지 강제로 떠밀린 게 아니다. `HttpClientFactory`의 `Json { }` 설정을 손댈 때 이 옵션을 추가하지 않는다.

## 1. DTO (`core/network/.../dto/*Response.kt`)

- **모든 필드 nullable + `= null` 기본값.** 예외 없이 일괄 적용한다 — "이 필드는 서버가 항상 채워줄 것 같다"는 식으로 필드마다 따로 판단하지 않는다.
- 유일한 예외: **그 응답이 존재할 이유 자체인 필수 필드**만 non-null, 기본값 없음. 없으면 응답 자체가 무의미해지는 값이라 조용히 넘어가지 말고 바로 크래시나야 한다. 두 종류뿐이다:
  - **그 레코드 자신의 식별 키**(그 레코드가 존재하려면 반드시 있어야 하는 PK). 다른 리소스를 가리키는 외래키(예: 댓글 DTO 안의 `postId`)는 여기 안 들어간다 — 그냥 nullable.
  - **그 응답의 존재 목적인 페이로드**(예: `TokenRefreshResponse.accessToken`/`refreshToken`). 토큰 없는 토큰 응답은 성공이 아니다.
- 이 예외를 "서버가 항상 채워줄 것 같은 필드"로 넓히지 않는다.
- `= ""` / `= 0` / `= false` / `= emptyList()` 같은 fake 기본값은 쓰지 않는다. `Boolean`도 예외 없이 nullable이다.

```kotlin
// Bad
@Serializable
data class AnimeDetailResponse(
    val animeId: Long = 0L,
    val title: String = "",
    val isLiked: Boolean = false,
    val reviewCount: Int = 0,
)

// Good - 실제 코드 (core/network/anime/dto/AnimeDetailResponse.kt)
@Serializable
data class AnimeDetailResponse(
    val animeId: Long,              // 이 리소스의 식별 키 - non-null, 기본값 없음
    val title: String? = null,
    val isLiked: Boolean? = null,
    val reviewCount: Int? = null,
    // ...
)

@Serializable
data class AnimeDetailStudioResponse(
    val studioId: Long,             // 중첩 아이템도 자기 식별 키는 non-null
    val name: String? = null,
)
```

## 2. Model (`core/model/.../*.kt`)

- DTO와 1:1로 미러링한다. 같은 필드가 nullable, 같은 "자기 식별 키만 non-null" 예외.
- Model이 nullable인 이유는 DTO와 다르다 — DTO는 "안 하면 역직렬화가 터지니까" 강제되는 것이고, Model은 "데이터가 없다"는 사실 자체를 view까지 그대로 들고 가기 위해서다. 그래서 Model이나 매퍼 안에서 `?: "-"`, `?: 0` 같은 placeholder를 미리 정해두지 않는다 — 그건 전적으로 view의 몫이다(아래 4번).
- 생성자 편의를 위해 `= null` 기본값을 붙이는 건 자유롭게 해도 된다(Preview mock 등에서 일부 필드만 채울 때 유용하다). 이 컨벤션에서 중요한 건 "기본값 문법을 쓰느냐"가 아니라 **"모델이 스스로 placeholder 값을 정하지 않는다"**는 것이다.

```kotlin
// Bad - 모델이 placeholder를 이미 정해버림
data class AnimeDetail(
    val animeId: Long,
    val title: String = "-",
    val isLiked: Boolean = false,
    val reviewCount: Int = 0,
)

// Good - 실제 코드 (core/model/anime/AnimeDetail.kt)
data class AnimeDetail(
    val animeId: Long,
    val title: String? = null,
    val isLiked: Boolean? = null,
    val reviewCount: Int? = null,
    // ...
)
```

## 3. 매퍼 (`fun XxxResponse.toXxx(): Xxx`)

- 순수 passthrough. `?:` 디폴팅을 하지 않는다. DTO에 있는 값을 그대로 model로 옮기기만 한다.

```kotlin
// Bad
fun AnimeDetailResponse.toAnimeDetail(): AnimeDetail = AnimeDetail(
    animeId = animeId,
    title = title ?: "-",
    isLiked = isLiked ?: false,
    reviewCount = reviewCount ?: 0,
)

// Good
fun AnimeDetailResponse.toAnimeDetail(): AnimeDetail = AnimeDetail(
    animeId = animeId,
    title = title,
    isLiked = isLiked,
    reviewCount = reviewCount,
)
```

## 4. View - null 처리는 여기서 화면별로 결정한다

Model까지는 항상 "값이 있거나 없거나"만 표현하고, 그걸 실제로 어떻게 보여줄지는 화면마다 다르다. 한 가지 규칙으로 통일하지 말고 그 자리에 맞는 처리를 고른다.

- 텍스트 하나를 그대로 보여주는 자리 → `title ?: "-"`
- 카운트/숫자 → `count ?: 0`
- 값이 없으면 그 섹션 자체를 안 그려야 하는 자리 → `value?.let { ... }`, `list.isNullOrEmpty()` 가드, `?.forEach { }`

같은 필드라도 화면에 따라 처리가 달라질 수 있다(예: 목록 카드에서는 숨기고, 상세 화면에서는 "-"로 보여주는 식). 그래서 이 판단을 Model이나 매퍼에 미리 박아두면 나중에 화면마다 다른 요구가 생겼을 때 다시 풀어내야 한다.

## 5. State - 컬렉션은 여기서 non-null로 편다

4번의 "화면이 정한다"는 **스칼라 값** 얘기다. 컬렉션은 예외로, **State에 담기는 시점(ViewModel)에서 non-null로 정규화한다.**

```kotlin
// ViewModel
.onSuccess { page ->
    val animes = page.animes ?: emptyList()          // 여기서 한 번만 편다
    _state.update { it.copy(animes = if (resetCursor) animes else it.animes + animes) }
}

// State
val animes: List<Anime> = emptyList(),   // 컬렉션은 non-null
val studioName: String? = null,          // 스칼라는 nullable 그대로
```

리스트를 nullable로 State까지 들고 가면 모든 Composable이 `?.`/`.orEmpty()`를 달아야 하는데, "리스트가 null"과 "리스트가 비었다"를 화면에서 다르게 그릴 일은 실제로 없다. 둘 다 "빈 상태"다. 반면 스칼라는 화면마다 `"-"`/숨김/기본값이 갈리므로 nullable을 유지한다.

## 6. 네이밍

- **도메인 엔티티**는 그냥 명사: `Anime`, `Review`, `Actor`, `Studio`.
- **커서 페이지네이션 목록**은 `CursorPage<T>`(`core/model/pagination/`)를 **재사용한다.** `cursor` + `items` + `count`뿐인 응답에 새 타입을 만들지 않는다. `count`는 안 내려주는 엔드포인트도 있어서 nullable이다.
  ```kotlin
  // Bad - CursorPage<Anime>와 모양이 같은데 매번 새 타입
  data class MyPageAnimesResult(val count: Int?, val cursor: Cursor?, val animes: List<Anime>?)

  // Good - 그대로 CursorPage를 쓴다
  suspend fun getLikedAnimes(...): Result<CursorPage<Anime>, DataError.Network>

  // Good - 부가 필드가 있으면 CursorPage를 품어서 합성한다
  data class SearchAnimePage(val animes: CursorPage<Anime>, val counts: SearchCounts, val nextPage: Long?)
  ```
  이렇게 두면 무한스크롤 처리(`cursor` 갱신, `endReached` 계산)를 화면마다 복붙하지 않고 한 모양으로 다룰 수 있다.
- **접미사**는 두 개만 쓴다: 페이지네이션이면 `~Page`, 한 화면을 채우려고 여러 리소스를 묶은 aggregate면 `~Result`.

## 7. enum 매핑

**보낼 때(요청 파라미터):** enum이 서버 문자열을 직접 들고 있게 한다. 호출부에서 문자열 리터럴을 쓰지 않는다.

```kotlin
// core/model/review/ReviewSort.kt
enum class ReviewSort(val apiValue: String) {
    LATEST("latest"),
    MOST_LIKED("like"),
    RATING_DESC("ratingDesc"),
    RATING_ASC("ratingAsc"),
}

// 호출부
sort = current.reviewSort.apiValue
```

**받을 때(응답 필드):** **DTO 필드를 enum 타입으로 직접 선언하지 않는다.** 서버가 새 값을 추가하는 순간 그 응답 전체의 역직렬화가 터진다. `String?`으로 받고 매퍼에서 매핑한다.

```kotlin
// DTO
val sort: String? = null

// 매퍼 - 모르는 값/null은 그냥 null로 떨어뜨린다
sort = ReviewSort.entries.find { it.apiValue == sort }
```

모르는 값을 위한 `UNKNOWN` fallback 상수를 만들지 않는다 — 그것도 결국 매퍼가 placeholder를 정하는 것이고, 4번 원칙에 어긋난다. 어떻게 보여줄지는 화면이 정한다.

> 현재 enum(`ReviewSort`, `ExploreSort`, `AnimeWatchStatus`)은 전부 요청 방향으로만 쓰고 있어서 응답 매핑 사례는 아직 없다. 응답에서 enum을 처음 받게 되는 사람이 위 규칙을 적용한다.

## 체크리스트

새 응답 DTO/모델을 추가할 때:

1. DTO의 모든 필드가 `Type? = null`인가? 그 응답의 식별 키/필수 페이로드만 예외로 non-null·기본값 없음인가?
2. Model이 DTO와 같은 nullable 구조를 그대로 미러링하는가?
3. 매퍼에 `?:` 디폴팅이 하나도 없는가? (`Boolean`도 예외 아님 - `?: false` 쓰지 않는다)
4. null을 "-"/0/숨김 중 무엇으로 보여줄지는 view 쪽 코드에서 화면별로 결정했는가?
5. State의 컬렉션은 ViewModel에서 `?: emptyList()`로 펴서 non-null로 담았는가? (스칼라는 nullable 유지)
6. 목록+커서뿐인 응답에 새 타입을 만들지 않고 `CursorPage<T>`를 재사용했는가? 접미사는 `~Page`/`~Result` 규칙을 따르는가?
7. enum으로 바꿀 문자열을 DTO에서 enum 타입으로 직접 받지 않았는가? (모르는 값 = 역직렬화 크래시)

## 참고

- 이 컨벤션을 그대로 따르는 기준 코드: `core/network/anime/dto/AnimeDetailResponse.kt` ↔ `core/model/anime/AnimeDetail.kt`.
- 이 컨벤션이 적용되기 전에 만들어진 일부 모델(`isXxx: Boolean = false` 같은 non-null 기본값이 남아있는 것들)은 아직 정리되지 않았을 수 있다. 새로 추가/수정하는 코드부터 이 규칙을 적용하고, 기존 코드는 손대는 김에 맞춰나간다.
