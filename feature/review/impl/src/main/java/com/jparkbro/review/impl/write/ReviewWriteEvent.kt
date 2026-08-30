package com.jparkbro.review.impl.write

sealed interface ReviewWriteEvent {
    data object SubmitSuccess : ReviewWriteEvent
}
