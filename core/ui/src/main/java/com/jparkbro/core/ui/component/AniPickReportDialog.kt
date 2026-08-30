package com.jparkbro.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jparkbro.core.designsystem.component.AniPickAnimatedChevronIcon
import com.jparkbro.core.designsystem.component.AniPickDialogFrame
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.report.ReportCategory

private const val PLACEHOLDER = "신고 유형 선택"
private const val EMPTY_SELECTION_MESSAGE = "신고 유형을 선택해주세요."

/** 게시글/댓글 신고 사유를 고르는 다이얼로그 - 뼈대와 버튼은 [AniPickDialogFrame]과 같고 본문만 다르다.
 *  유형을 고르지 않고 확인을 누르면 [onConfirm] 대신 셀렉트 박스 아래에 경고 문구를 띄운다. */
@Composable
fun AniPickReportDialog(
    selectedCategory: ReportCategory?,
    onCategorySelect: (ReportCategory) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "신고",
    confirmText: String = "신고하기",
    dismissText: String = "닫기",
) {
    Dialog(onDismissRequest = onDismissRequest) {
        AniPickReportDialogContent(
            selectedCategory = selectedCategory,
            onCategorySelect = onCategorySelect,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            modifier = modifier,
            title = title,
            confirmText = confirmText,
            dismissText = dismissText,
        )
    }
}

/** [Dialog]는 Preview에 렌더링되지 않아 실제 콘텐츠를 분리한 것 - Preview에서는 이 함수를 직접 호출한다. */
@Composable
private fun AniPickReportDialogContent(
    selectedCategory: ReportCategory?,
    onCategorySelect: (ReportCategory) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "신고",
    confirmText: String = "신고하기",
    dismissText: String = "닫기",
    initiallyExpanded: Boolean = false,
    initialErrorMessage: String? = null,
) {
    // 펼침 여부와 미선택 경고는 다이얼로그가 닫히면 사라지는 값이라 화면 State로 올리지 않는다.
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    var errorMessage by remember { mutableStateOf(initialErrorMessage) }

    AniPickDialogFrame(
        title = title,
        confirmText = confirmText,
        onConfirm = {
            if (selectedCategory == null) errorMessage = EMPTY_SELECTION_MESSAGE else onConfirm()
        },
        modifier = modifier,
        dismissText = dismissText,
        onDismiss = onDismiss,
    ) {
        Text(
            text = PLACEHOLDER,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
        )
        ReportCategorySelectBox(
            selectedCategory = selectedCategory,
            isExpanded = isExpanded,
            onToggle = { isExpanded = !isExpanded },
        )
        AniPickErrorText(message = errorMessage, modifier = Modifier.fillMaxWidth())
        if (isExpanded) {
            ReportCategoryList(
                onCategorySelect = { category ->
                    onCategorySelect(category)
                    isExpanded = false
                    errorMessage = null
                },
            )
        }
    }
}

@Composable
private fun ReportCategorySelectBox(
    selectedCategory: ReportCategory?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.lightGray)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = selectedCategory?.label ?: PLACEHOLDER,
            style = AniPickTheme.typography.body2,
            color = if (selectedCategory != null) AniPickTheme.colors.black else AniPickTheme.colors.textGray,
        )
        AniPickAnimatedChevronIcon(
            isExpanded = isExpanded,
            modifier = Modifier.size(20.dp),
            tint = AniPickTheme.colors.black,
        )
    }
}

@Composable
private fun ReportCategoryList(
    onCategorySelect: (ReportCategory) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.lightGray),
    ) {
        ReportCategory.entries.forEach { category ->
            Text(
                text = category.label,
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.black,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onCategorySelect(category) })
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}

private val ReportCategory.label: String
    get() = when (this) {
        ReportCategory.ABUSE -> "욕설/비하/혐오 표현"
        ReportCategory.PRIVACY -> "개인정보 노출"
        ReportCategory.SPAM -> "도배/스팸/광고성 내용"
        ReportCategory.ILLEGAL -> "불법/유해/부적절한 내용"
        ReportCategory.ETC -> "기타 운영정책 위반"
    }

@Preview(showBackground = true)
@Composable
private fun AniPickReportDialogCollapsedPreview() {
    AniPickReportDialogContent(
        selectedCategory = null,
        onCategorySelect = {},
        onConfirm = {},
        onDismiss = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickReportDialogExpandedPreview() {
    AniPickReportDialogContent(
        selectedCategory = null,
        onCategorySelect = {},
        onConfirm = {},
        onDismiss = {},
        initiallyExpanded = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickReportDialogSelectedPreview() {
    AniPickReportDialogContent(
        selectedCategory = ReportCategory.SPAM,
        onCategorySelect = {},
        onConfirm = {},
        onDismiss = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickReportDialogErrorPreview() {
    AniPickReportDialogContent(
        selectedCategory = null,
        onCategorySelect = {},
        onConfirm = {},
        onDismiss = {},
        initialErrorMessage = EMPTY_SELECTION_MESSAGE,
    )
}
