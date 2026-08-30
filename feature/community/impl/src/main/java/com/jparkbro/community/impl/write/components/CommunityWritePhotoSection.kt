package com.jparkbro.community.impl.write.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.community.impl.write.MAX_IMAGE_COUNT
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.icon.Image
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 사진 첨부 - 선택된 사진 미리보기 + 추가 타일 + 최대 장수/용량 안내 */
@Composable
internal fun CommunityWritePhotoSection(
    images: List<Uri>,
    onAddClick: () -> Unit,
    onRemoveClick: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(images) { uri ->
                SelectedPhotoThumbnail(
                    uri = uri,
                    onRemoveClick = { onRemoveClick(uri) },
                )
            }
            if (images.size < MAX_IMAGE_COUNT) {
                item {
                    AddPhotoTile(onClick = onAddClick)
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "• 사진은 최대 ${MAX_IMAGE_COUNT}장까지 등록 가능합니다.",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
            Text(
                text = "• 사진은 장당 10MB까지 첨부할 수 있어요.",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Composable
private fun SelectedPhotoThumbnail(
    uri: Uri,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(112.dp)) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(8.dp)),
        )
        Icon(
            imageVector = Close,
            contentDescription = "사진 삭제",
            tint = AniPickTheme.colors.white,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onRemoveClick)
                .padding(2.dp),
        )
    }
}

@Composable
private fun AddPhotoTile(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(112.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.lightGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Image,
            contentDescription = "사진 추가",
            tint = AniPickTheme.colors.textGray,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityWritePhotoSectionPreview() {
    CommunityWritePhotoSection(
        images = emptyList(),
        onAddClick = {},
        onRemoveClick = {},
    )
}
