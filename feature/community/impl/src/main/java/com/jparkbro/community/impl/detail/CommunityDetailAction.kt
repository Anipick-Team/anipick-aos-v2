package com.jparkbro.community.impl.detail

import com.jparkbro.core.model.report.ReportCategory

sealed interface CommunityDetailAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CommunityDetailAction

    data object OnBackClick : Navigation
    data object OnCommentIconClick : CommunityDetailAction
    data object OnShareClick : CommunityDetailAction
    data class OnCommentReplyClick(val commentId: Long) : CommunityDetailAction
    data object OnReplyTargetCancelClick : CommunityDetailAction
    data object OnRetryClick : CommunityDetailAction
    data object OnEditClick : Navigation
    data object OnDeleteClick : CommunityDetailAction
    data object OnDeleteConfirm : CommunityDetailAction
    data object OnDeleteDismiss : CommunityDetailAction
    data object OnReportClick : CommunityDetailAction
    data class OnReportCategorySelect(val category: ReportCategory) : CommunityDetailAction
    data object OnReportConfirm : CommunityDetailAction
    data object OnReportDismiss : CommunityDetailAction
    data object OnPostLikeClick : CommunityDetailAction
    data object OnCommentSendClick : CommunityDetailAction
    data class OnCommentLikeClick(val commentId: Long) : CommunityDetailAction
    data class OnCommentDeleteClick(val commentId: Long) : CommunityDetailAction
    data object OnCommentDeleteConfirm : CommunityDetailAction
    data object OnCommentDeleteDismiss : CommunityDetailAction
    data class OnCommentReportClick(val commentId: Long) : CommunityDetailAction
}
