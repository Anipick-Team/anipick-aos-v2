package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 라벨 아래에 텍스트필드를 배치하는 공용 폼 필드 레이아웃 - 텍스트필드는 슬롯으로 받는다. */
@Composable
fun AniPickLabeledField(
    label: String,
    modifier: Modifier = Modifier,
    labelTrailingContent: (@Composable () -> Unit)? = null,
    fieldTrailingContent: (@Composable () -> Unit)? = null,
    textField: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
            labelTrailingContent?.invoke()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                textField()
            }
            fieldTrailingContent?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickLabeledFieldPreview() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AniPickLabeledField(
            label = "이메일",
            textField = {
                AniPickBaseTextField(
                    state = rememberTextFieldState(),
                    type = TextFieldType.TEXT,
                    placeholder = "이메일을 입력하세요",
                )
            },
        )

        AniPickLabeledField(
            label = "닉네임",
            labelTrailingContent = {
                Text(
                    text = "2~10자",
                    style = AniPickTheme.typography.caption2,
                    color = AniPickTheme.colors.textGray,
                )
            },
            fieldTrailingContent = {
                Text(
                    text = "중복확인",
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.primary,
                )
            },
            textField = {
                AniPickBaseTextField(
                    state = rememberTextFieldState(),
                    type = TextFieldType.TEXT,
                    placeholder = "닉네임을 입력하세요",
                )
            },
        )
    }
}
