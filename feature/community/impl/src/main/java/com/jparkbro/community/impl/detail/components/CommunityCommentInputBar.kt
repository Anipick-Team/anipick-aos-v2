package com.jparkbro.community.impl.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.icon.ArrowRight
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.icon.Send
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 게시글 상세 최하단 댓글 입력 바 - 유튜브/카카오톡 댓글창처럼 화면 하단에 고정해서 쓴다.
 *  [replyTargetContent]가 있으면 상단 divider 위로 답글 대상 미리보기 행을 보여준다. */
@Composable
internal fun CommunityCommentInputBar(
    state: TextFieldState,
    enabled: Boolean,
    isSubmitting: Boolean,
    replyTargetContent: String?,
    onCancelReplyClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.white)
            // edge-to-edge라 직접 처리해야 함 - 키보드 닫혀있으면 네비게이션 바 위로, 열려있으면 키보드 위로.
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)),
    ) {
        if (replyTargetContent != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ArrowRight,
                    contentDescription = null,
                    tint = AniPickTheme.colors.black,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = replyTargetContent,
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Close,
                    contentDescription = "답글 취소",
                    tint = AniPickTheme.colors.black,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onCancelReplyClick),
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = AniPickTheme.colors.backgroundGray)
        AniPickBaseTextField(
            state = state,
            placeholder = "댓글을 작성해 주세요.",
            actions = {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = AniPickTheme.colors.primary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Send,
                        contentDescription = "등록",
                        tint = if (enabled) AniPickTheme.colors.primary else AniPickTheme.colors.gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(enabled = enabled, onClick = onSendClick),
                    )
                }
            },
            maxLength = 200,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityCommentInputBarPreview() {
    CommunityCommentInputBar(
        state = rememberTextFieldState(),
        enabled = false,
        isSubmitting = false,
        replyTargetContent = null,
        onCancelReplyClick = {},
        onSendClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityCommentInputBarReplyPreview() {
    CommunityCommentInputBar(
        state = rememberTextFieldState(),
        enabled = false,
        isSubmitting = false,
        replyTargetContent = "저도 이번 화 보고 소름 돋았어요 ㅋㅋ 진짜 연출 미쳤음",
        onCancelReplyClick = {},
        onSendClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CommunityCommentInputBarSubmittingPreview() {
    CommunityCommentInputBar(
        state = rememberTextFieldState(),
        enabled = false,
        isSubmitting = true,
        replyTargetContent = null,
        onCancelReplyClick = {},
        onSendClick = {},
    )
}
