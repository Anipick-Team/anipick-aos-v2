package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** "총 {count}{unit}" 형태로 개수를 보여준다 - count만 primary 색으로 강조. */
@Composable
fun AniPickCountLabel(
    count: Int,
    unit: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(top = 10.dp, bottom = 4.dp),
) {
    Text(
        text = buildAnnotatedString {
            append("총 ")
            withStyle(SpanStyle(color = AniPickTheme.colors.primary)) {
                append("$count")
            }
            append(" $unit")
        },
        style = AniPickTheme.typography.body2,
        color = AniPickTheme.colors.black,
        modifier = modifier.padding(contentPadding),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickCountLabelPreview() {
    AniPickCountLabel(count = 12, unit = "개")
}
