package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.icon.Search
import com.jparkbro.core.designsystem.icon.VisibilityOff
import com.jparkbro.core.designsystem.icon.VisibilityOn
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.designsystem.transformation.AmountOutputTransformation
import com.jparkbro.core.designsystem.transformation.DateOutputTransformation
import com.jparkbro.core.designsystem.transformation.DigitsOnlyInputTransformation
import com.jparkbro.core.designsystem.transformation.PasswordOutputTransformation
import com.jparkbro.core.designsystem.transformation.PhoneNumberOutputTransformation
import com.jparkbro.core.designsystem.transformation.TrimInputTransformation

/** 기본 텍스트필드 - [TextFieldType]에 따라 입력/출력 트랜스포메이션과 키보드 타입이 자동 설정된다. */
@Composable
fun AniPickBaseTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPickTheme.typography.body2.copy(color = AniPickTheme.colors.black),
    type: TextFieldType = TextFieldType.TEXT,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    actions: @Composable (() -> Unit)? = null,
    showPassword: Boolean = false,
    maxLength: Int? = null,
    height: Dp = 46.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    val isNumericType = type == TextFieldType.PHONE || type == TextFieldType.NUMBER || type == TextFieldType.DATE

    val inputTransformation = listOfNotNull(
        if (type == TextFieldType.TEXT) TrimInputTransformation else null,
        if (isNumericType) DigitsOnlyInputTransformation else null,
        maxLength?.let { InputTransformation.maxLength(it) },
    ).reduceOrNull { acc, next -> acc.then(next) }

    val outputTransformation = when (type) {
        TextFieldType.PASSWORD -> if (showPassword) null else PasswordOutputTransformation
        TextFieldType.PHONE -> PhoneNumberOutputTransformation
        TextFieldType.NUMBER -> AmountOutputTransformation
        TextFieldType.DATE -> DateOutputTransformation
        TextFieldType.TEXT -> null
    }

    val resolvedKeyboardOptions = when {
        isNumericType -> keyboardOptions.copy(keyboardType = KeyboardType.Number)
        type == TextFieldType.PASSWORD -> keyboardOptions.copy(keyboardType = KeyboardType.Password)
        else -> keyboardOptions
    }

    BasicTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = resolvedKeyboardOptions,
        onKeyboardAction = {
            onKeyboardAction()
        },
        lineLimits = lineLimits,
        cursorBrush = SolidColor(AniPickTheme.colors.primary),
        outputTransformation = outputTransformation,
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(AniPickTheme.colors.lightGray, RoundedCornerShape(8.dp))
                    .padding(contentPadding),
                verticalAlignment = verticalAlignment,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = if (verticalAlignment == Alignment.Top) Alignment.TopStart else Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = AniPickTheme.colors.textGray))
                    }
                    innerTextField()
                }
                actions?.let { action ->
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        action()
                    }
                }
            }
        },
    )
}

/** 검색 전용 필드 - Enter/검색 아이콘으로 [onSearchClick], X 아이콘으로 [onClearClick]을 호출한다. */
@Composable
fun AniPickSearchTextField(
    state: TextFieldState,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "무엇을 검색할까요?",
) {
    val focusManager = LocalFocusManager.current

    AniPickBaseTextField(
        state = state,
        modifier = modifier,
        type = TextFieldType.TEXT,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = {
            focusManager.clearFocus()
            onSearchClick()
        },
        placeholder = placeholder,
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Search,
                    tint = AniPickTheme.colors.textGray,
                    contentDescription = "검색",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            focusManager.clearFocus()
                            onSearchClick()
                        }
                )
                Icon(
                    imageVector = Close,
                    tint = AniPickTheme.colors.textGray,
                    contentDescription = "검색 초기화",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onClearClick)
                )
            }
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun AniPickSearchTextFieldPreview() {
    AniPickSearchTextField(
        state = rememberTextFieldState(),
        onSearchClick = {},
        onClearClick = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun AniPickBaseTextFieldPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AniPickBaseTextField(
            state = rememberTextFieldState(),
            textStyle = AniPickTheme.typography.body2,
            type = TextFieldType.TEXT,
            placeholder = "이메일을 입력하세요"
        )

        var showPassword by remember { mutableStateOf(false) }
        AniPickBaseTextField(
            state = rememberTextFieldState(),
            textStyle = AniPickTheme.typography.body2,
            type = TextFieldType.PASSWORD,
            placeholder = "비밀번호를 입력하세요",
            showPassword = showPassword,
            actions = {
                Icon(
                    imageVector = if (showPassword) VisibilityOn else VisibilityOff,
                    contentDescription = "비밀번호 표시 전환",
                    tint = AniPickTheme.colors.textGray
                )
            }
        )

        AniPickBaseTextField(
            state = rememberTextFieldState(),
            textStyle = AniPickTheme.typography.body2,
            type = TextFieldType.PHONE,
            placeholder = "전화번호를 입력하세요"
        )

        AniPickBaseTextField(
            state = rememberTextFieldState(),
            textStyle = AniPickTheme.typography.body2,
            type = TextFieldType.NUMBER,
            placeholder = "금액을 입력하세요"
        )

        AniPickBaseTextField(
            state = rememberTextFieldState(),
            textStyle = AniPickTheme.typography.body2,
            type = TextFieldType.DATE,
            placeholder = "생년월일을 입력하세요"
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AniPickBaseTextFieldDisabledPreview() {
    AniPickBaseTextField(
        modifier = Modifier.padding(16.dp),
        state = rememberTextFieldState("비활성 상태"),
        textStyle = AniPickTheme.typography.body2,
        enabled = false
    )
}