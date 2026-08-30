package com.jparkbro.review.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ReviewNavKey : NavKey {

    /** 최근 리뷰 전체보기 - 홈 메인의 "최근 리뷰" 더보기에서 진입한다. */
    @Serializable
    data object Recent : ReviewNavKey

    /** 리뷰 작성/수정 - [animeId] 기준. 기존 리뷰 있으면 수정 모드, 없으면 작성 모드로 자동 분기 */
    @Serializable
    data class Write(val animeId: Long) : ReviewNavKey

    /** 마이페이지 "평가한 작품" 전체보기 - MyPageMain "평가한 작품" 더보기에서 진입한다. */
    @Serializable
    data object Rated : ReviewNavKey
}
