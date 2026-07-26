package com.jparkbro.anipick.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jparkbro.core.designsystem.R
import com.jparkbro.explore.api.ExploreNavKey
import com.jparkbro.home.api.HomeNavKey
import com.jparkbro.mypage.api.MyPageNavKey
import com.jparkbro.ranking.api.RankingNavKey
import com.jparkbro.splash.api.SplashNavKey

data class TopLevelNavItem(
    @param:DrawableRes val selectedIcon: Int = 0,
    @param:DrawableRes val unselectedIcon: Int = 0,
    @param:StringRes val iconTextId: Int = 0,
)

val HOME = TopLevelNavItem(
    selectedIcon = R.drawable.ic_bottom_nav_1_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_1_unselected,
    iconTextId = R.string.home,
)

val RANKING = TopLevelNavItem(
    selectedIcon = R.drawable.ic_bottom_nav_2_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_2_unselected,
    iconTextId = R.string.ranking,
)

val EXPLORE = TopLevelNavItem(
    selectedIcon = R.drawable.ic_bottom_nav_3_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_3_unselected,
    iconTextId = R.string.explore,
)

val MY_PAGE = TopLevelNavItem(
    selectedIcon = R.drawable.ic_bottom_nav_4_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_4_unselected,
    iconTextId = R.string.my_page,
)

val SPLASH = TopLevelNavItem()

val TOP_LEVEL_ITEMS = mapOf(
    SplashNavKey.Splash to SPLASH,
    HomeNavKey.Main to HOME,
    RankingNavKey.Ranking to RANKING,
    ExploreNavKey.Explore to EXPLORE,
    MyPageNavKey.Main to MY_PAGE,
)

val BOTTOM_NAV_ITEMS = mapOf(
    HomeNavKey.Main to HOME,
    RankingNavKey.Ranking to RANKING,
    ExploreNavKey.Explore to EXPLORE,
    MyPageNavKey.Main to MY_PAGE,
)