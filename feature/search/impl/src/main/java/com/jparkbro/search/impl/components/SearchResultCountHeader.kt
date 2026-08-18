package com.jparkbro.search.impl.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun SearchResultCountHeader(
    count: Int,
    unit: String,
    modifier: Modifier = Modifier,
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
        modifier = modifier.padding(top = 10.dp, bottom = 4.dp),
    )
}
