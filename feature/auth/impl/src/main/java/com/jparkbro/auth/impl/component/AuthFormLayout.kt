package com.jparkbro.auth.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** auth 하위 폼 화면들이 공통으로 쓰는 뼈대 - 헤더 + [fields] + [bottom] 배치 */
@Composable
internal fun AuthFormLayout(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    fields: @Composable ColumnScope.() -> Unit,
) {
    AuthScaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp, 20.dp, 20.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp),
                content = {
                    AuthScreenHeader(
                        title = title,
                        subtitle = subtitle,
                        onBackClick = onBackClick,
                    )
                    fields()
                },
            )
            bottom()
        }
    }
}
