package com.jparkbro.community.impl.write

sealed interface CommunityWriteEvent {
    data object WriteSuccess : CommunityWriteEvent
}
