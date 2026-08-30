package com.jparkbro.mypage.impl.main

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
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.mypage.api.MyPageDetailType
import com.jparkbro.mypage.impl.main.components.MyPageTopAppBar
import com.jparkbro.mypage.impl.main.components.myPageMainSections
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MyPageMainRoot(
    bottomNavigation: @Composable () -> Unit,
    onNavigateToDetail: (MyPageDetailType) -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToActorDetail: (Long) -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToRatedAnimes: () -> Unit,
    viewModel: MyPageMainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyPageMainScreen(
        state = state,
        bottomNavigation = bottomNavigation,
        onAction = { action ->
            when (action) {
                is MyPageMainAction.Navigation -> when (action) {
                    is MyPageMainAction.OnDetailClick -> onNavigateToDetail(action.type)
                    is MyPageMainAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                    is MyPageMainAction.OnPersonClick -> onNavigateToActorDetail(action.personId)
                    MyPageMainAction.OnSettingClick -> onNavigateToSetting()
                    MyPageMainAction.OnRatedAnimesClick -> onNavigateToRatedAnimes()
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun MyPageMainScreen(
    state: MyPageMainState,
    bottomNavigation: @Composable () -> Unit,
    onAction: (MyPageMainAction) -> Unit,
) {
    Scaffold(
        topBar = {
            MyPageTopAppBar(
                profileImage = state.profileImageUrl,
                onSettingClick = { onAction(MyPageMainAction.OnSettingClick) },
                onProfileImageSelected = { image -> onAction(MyPageMainAction.OnChangeProfileImage(image)) },
            )
        },
        bottomBar = bottomNavigation,
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            myPageMainSections(state = state, onAction = onAction)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MyPageMainScreenPreview() {
    MyPageMainScreen(
        state = MyPageMainState(),
        bottomNavigation = {},
        onAction = {},
    )
}
