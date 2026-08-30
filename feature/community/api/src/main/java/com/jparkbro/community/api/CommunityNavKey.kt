package com.jparkbro.community.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface CommunityNavKey : NavKey {

    /** 특정 애니 커뮤니티 메인 - 게시글 목록 조회는 [seriesId] 기준이다.
     *  [title]/[coverImageUrl]/[genres]는 상단 애니 정보 섹션 표시용 - 진입하는 쪽(애니 상세의 `CommunityBoard`
     *  조회 결과, Explore 게시판 목록의 `CommunityBoard`)이 이미 들고 있는 값을 그대로 넘겨받는다. 별도로 다시
     *  조회하지 않는다 - 그래서 여기 값 그대로가 화면에 뜨는 값과 같다. */
    @Serializable
    data class Main(
        val seriesId: Long,
        val title: String? = null,
        val coverImageUrl: String? = null,
        val genres: List<String>? = null,
    ) : CommunityNavKey

    /** 게시글 상세. */
    @Serializable
    data class Detail(val postId: Long) : CommunityNavKey

    /** 게시글 등록/수정 - [seriesId] 게시판 기준. [postId]가 있으면 그 게시글을 수정 모드로 연다. */
    @Serializable
    data class Write(val seriesId: Long, val postId: Long? = null) : CommunityNavKey
}
