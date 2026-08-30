package com.jparkbro.community.impl.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.community.impl.write.components.communityWriteFields
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.component.AniPickTopBarSubmitAction
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.effect.ObserveAsEvents
import com.jparkbro.core.ui.effect.rememberPhotoPickerWithPermission
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CommunityWriteRoot(
    seriesId: Long,
    postId: Long? = null,
    onBackClick: () -> Unit,
    onWriteSuccess: () -> Unit,
    viewModel: CommunityWriteViewModel = koinViewModel(parameters = { parametersOf(seriesId, postId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CommunityWriteEvent.WriteSuccess -> onWriteSuccess()
        }
    }

    CommunityWriteScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CommunityWriteAction.Navigation -> when (action) {
                    CommunityWriteAction.OnBackClick -> onBackClick()
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CommunityWriteScreen(
    state: CommunityWriteState,
    onAction: (CommunityWriteAction) -> Unit,
) {
    val pickPhoto = rememberPhotoPickerWithPermission { uri ->
        onAction(CommunityWriteAction.OnImageAdd(uri))
    }

    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = if (state.isEditMode) "글 수정" else "글 작성",
                onBackClick = { onAction(CommunityWriteAction.OnBackClick) },
                actions = {
                    AniPickTopBarSubmitAction(
                        text = if (state.isEditMode) "수정" else "등록",
                        enabled = state.isSubmitEnabled && !state.isSubmitting,
                        onClick = { onAction(CommunityWriteAction.OnSubmitClick) },
                    )
                },
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            communityWriteFields(state = state, onAction = onAction, onAddPhotoClick = pickPhoto)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CommunityWriteScreenPreview() {
    CommunityWriteScreen(
        state = CommunityWriteState(seriesId = 1L),
        onAction = {},
    )
}
