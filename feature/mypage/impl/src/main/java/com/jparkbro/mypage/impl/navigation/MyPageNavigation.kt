package com.jparkbro.mypage.impl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.community.api.CommunityNavKey
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.mypage.api.MyPageNavKey
import com.jparkbro.mypage.impl.detail.MyPageDetailRoot
import com.jparkbro.mypage.impl.main.MyPageMainRoot
import com.jparkbro.mypage.impl.setting.detail.SettingDetailRoot
import com.jparkbro.mypage.impl.setting.main.SettingMainRoot
import com.jparkbro.review.api.ReviewNavKey

const val MYPAGE_MAIN_CONTENT_KEY = "MyPageNavKey.Main"
const val MYPAGE_DETAIL_CONTENT_KEY = "MyPageNavKey.Detail"
const val MYPAGE_SETTING_MAIN_CONTENT_KEY = "MyPageNavKey.Setting.Main"
const val MYPAGE_SETTING_DETAIL_CONTENT_KEY = "MyPageNavKey.Setting.Detail"

fun EntryProviderScope<NavKey>.myPageEntry(
    navigator: Navigator,
    bottomNavigation: @Composable () -> Unit,
    onWithdrawSuccess: () -> Unit,
    onLogout: () -> Unit,
) {
    entry<MyPageNavKey.Main>(clazzContentKey = { MYPAGE_MAIN_CONTENT_KEY }) {
        MyPageMainRoot(
            bottomNavigation = bottomNavigation,
            onNavigateToDetail = { type -> navigator.navigate(MyPageNavKey.Detail(type)) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToActorDetail = { personId -> navigator.navigate(CatalogNavKey.Actor(personId)) },
            onNavigateToSetting = { navigator.navigate(MyPageNavKey.Setting.Main) },
            onNavigateToRatedAnimes = { navigator.navigate(ReviewNavKey.Rated) },
        )
    }
    entry<MyPageNavKey.Detail>(clazzContentKey = { MYPAGE_DETAIL_CONTENT_KEY }) { key ->
        MyPageDetailRoot(
            type = key.type,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToActorDetail = { personId -> navigator.navigate(CatalogNavKey.Actor(personId)) },
            onNavigateToPostDetail = { postId -> navigator.navigate(CommunityNavKey.Detail(postId)) },
        )
    }
    entry<MyPageNavKey.Setting.Main>(clazzContentKey = { MYPAGE_SETTING_MAIN_CONTENT_KEY }) {
        SettingMainRoot(
            onBackClick = navigator::goBack,
            onNavigateToDetail = { type -> navigator.navigate(MyPageNavKey.Setting.Detail(type)) },
            onLogout = onLogout,
        )
    }
    entry<MyPageNavKey.Setting.Detail>(clazzContentKey = { MYPAGE_SETTING_DETAIL_CONTENT_KEY }) { key ->
        SettingDetailRoot(
            type = key.type,
            onBackClick = navigator::goBack,
            onWithdrawSuccess = onWithdrawSuccess,
        )
    }
}
