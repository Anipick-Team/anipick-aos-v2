package com.jparkbro.core.ui.effect

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import android.net.Uri

/** 갤러리에서 이미지 선택 후 [onImageSelected] 호출하는 런처 반환 */
@Composable
fun rememberPhotoPickerWithPermission(
    onImageSelected: (Uri) -> Unit
): () -> Unit {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contentUri ->
        if (contentUri != null) {
            onImageSelected(contentUri)
        }
    }

    return {
        photoPicker.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }
}
