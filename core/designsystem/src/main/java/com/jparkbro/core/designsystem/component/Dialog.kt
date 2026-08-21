package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 확인/취소 버튼 구성이 가능한 공용 다이얼로그
 *  [dismissText] null: 확인 버튼만 있는 Alert, 있음: 확인+취소 버튼이 있는 Confirm */
@Composable
fun AniPickDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        AniPickDialogContent(
            title = title,
            message = message,
            confirmText = confirmText,
            onConfirm = onConfirm,
            modifier = modifier,
            dismissText = dismissText,
            onDismiss = onDismiss,
        )
    }
}

/** [Dialog]는 Preview에 렌더링되지 않아 실제 콘텐츠를 분리한 것 - Preview에서는 이 함수를 직접 호출한다. */
@Composable
private fun AniPickDialogContent(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(36.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
            Text(
                text = message,
                style = AniPickTheme.typography.caption1,
                color = AniPickTheme.colors.textGray,
            )
        }

        if (dismissText != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dismissText,
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.gray,
                    modifier = Modifier
                        .clickable(onClick = { onDismiss?.invoke() })
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(12.dp),
                    thickness = 1.dp,
                    color = AniPickTheme.colors.textGray
                )
                Text(
                    text = confirmText,
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.primary,
                    modifier = Modifier
                        .clickable(onClick = { onConfirm() })
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        } else {
            Text(
                text = confirmText,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.primary,
                modifier = Modifier
                    .clickable(onClick = { onConfirm() })
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickDialogAlertPreview() {
    AniPickDialogContent(
        title = "로그인 실패",
        message = "이메일 또는 비밀번호가 일치하지 않습니다.",
        confirmText = "확인",
        onConfirm = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickDialogConfirmPreview() {
    AniPickDialogContent(
        title = "로그아웃",
        message = "정말 로그아웃 하시겠어요?",
        confirmText = "로그아웃",
        onConfirm = {},
        dismissText = "취소",
        onDismiss = {},
    )
}
