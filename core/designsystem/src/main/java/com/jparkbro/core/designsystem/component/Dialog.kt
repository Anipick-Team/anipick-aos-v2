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
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * 확인/취소 버튼 구성이 가능한 공용 다이얼로그.
 * [dismissText]가 null이면 확인 버튼 하나만 있는 Alert 형태, 값이 있으면 확인+취소 버튼이 있는 Confirm 형태로 쓴다.
 *
 * @param title 다이얼로그 상단 제목
 * @param message 본문 설명 텍스트
 * @param onDismissRequest 다이얼로그 바깥 영역 탭 또는 시스템 뒤로가기로 닫힐 때 호출된다
 * @param confirmText 확인(주 액션) 버튼에 표시할 텍스트
 * @param onConfirm 확인 버튼 클릭 시 실행할 동작
 * @param modifier 다이얼로그 콘텐츠에 적용할 [Modifier]
 * @param dismissText 취소(보조 액션) 버튼 텍스트. null이면 취소 버튼을 그리지 않아 Alert 형태가 된다
 * @param onDismiss 취소 버튼 클릭 시 실행할 동작. [dismissText]가 null이면 쓰이지 않는다
 */
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

/**
 * [Dialog]는 별도의 시스템 윈도우로 떠서 Compose Preview에 내용이 렌더링되지 않는다.
 * 그래서 실제 콘텐츠(카드+버튼)를 이 컴포저블로 분리해서, Preview에서는 [Dialog] 없이 이 함수를 직접 호출한다.
 */
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
                style = AniPickTheme.typography.body2,
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
