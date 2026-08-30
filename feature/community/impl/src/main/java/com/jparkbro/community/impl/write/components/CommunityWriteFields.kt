package com.jparkbro.community.impl.write.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.community.impl.write.CONTENT_MAX_LENGTH
import com.jparkbro.community.impl.write.CommunityWriteAction
import com.jparkbro.community.impl.write.CommunityWriteState
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickErrorText
import com.jparkbro.core.designsystem.component.AniPickLabeledField
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 제목/내용/스포일러/사진 입력 필드 - CommunityWriteScreen LazyColumn의 item들. */
internal fun LazyListScope.communityWriteFields(
    state: CommunityWriteState,
    onAction: (CommunityWriteAction) -> Unit,
    onAddPhotoClick: () -> Unit,
) {
    item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AniPickLabeledField(
                label = "제목",
                textField = {
                    AniPickBaseTextField(
                        state = state.titleState,
                        type = TextFieldType.TEXT,
                        placeholder = "제목을 입력해 주세요.",
                        maxLength = 50
                    )
                },
            )
            state.titleError?.let { AniPickErrorText(it) }
        }
    }
    item {
        AniPickLabeledField(
            label = "내용",
            textField = {
                Box {
                    AniPickBaseTextField(
                        state = state.contentState,
                        type = TextFieldType.TEXT,
                        placeholder = "리뷰 내용을 입력해주세요.",
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 6, maxHeightInLines = 10),
                        maxLength = CONTENT_MAX_LENGTH,
                        height = 220.dp,
                        verticalAlignment = Alignment.Top,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 16.dp + 20.dp + 8.dp,
                        ),
                    )
                    Text(
                        text = "${state.contentState.text.length}/$CONTENT_MAX_LENGTH",
                        style = AniPickTheme.typography.caption2,
                        color = AniPickTheme.colors.textGray,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                    )
                }
            },
        )
    }
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "스포일러",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.primary,
            )
            AniPickSwitch(
                checked = state.isSpoiler,
                onCheckedChange = { onAction(CommunityWriteAction.OnSpoilerToggle(it)) },
            )
        }
    }
    item {
        CommunityWritePhotoSection(
            images = state.images,
            onAddClick = onAddPhotoClick,
            onRemoveClick = { uri -> onAction(CommunityWriteAction.OnImageRemove(uri)) },
        )
    }
}
