package com.jparkbro.mypage.api

import kotlinx.serialization.Serializable

@Serializable
sealed interface MyPageDetailType {

    @Serializable
    data object WatchList : MyPageDetailType

    @Serializable
    data object Watching : MyPageDetailType

    @Serializable
    data object Finished : MyPageDetailType

    @Serializable
    data object LikedAnimes : MyPageDetailType

    @Serializable
    data object LikedPersons : MyPageDetailType

    @Serializable
    data object MyContent : MyPageDetailType
}
