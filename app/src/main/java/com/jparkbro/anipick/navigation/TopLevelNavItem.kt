package com.jparkbro.anipick.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jparkbro.core.designsystem.R
import com.jparkbro.explore.api.ExploreNavKey
import com.jparkbro.explore.impl.navigation.EXPLORE_CONTENT_KEY
import com.jparkbro.home.api.HomeNavKey
import com.jparkbro.home.impl.navigation.HOME_MAIN_CONTENT_KEY
import com.jparkbro.mypage.api.MyPageNavKey
import com.jparkbro.mypage.impl.navigation.MYPAGE_MAIN_CONTENT_KEY
import com.jparkbro.ranking.api.RankingNavKey
import com.jparkbro.ranking.impl.navigation.RANKING_CONTENT_KEY
import com.jparkbro.splash.api.SplashNavKey
import com.jparkbro.splash.impl.navigation.SPLASH_CONTENT_KEY

data class TopLevelNavItem(
    val contentKey: String,
    @param:DrawableRes val selectedIcon: Int = 0,
    @param:DrawableRes val unselectedIcon: Int = 0,
    @param:StringRes val iconTextId: Int = 0,
)

val HOME = TopLevelNavItem(
    contentKey = HOME_MAIN_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_1_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_1_unselected,
    iconTextId = R.string.home,
)

val RANKING = TopLevelNavItem(
    contentKey = RANKING_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_2_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_2_unselected,
    iconTextId = R.string.ranking,
)

val EXPLORE = TopLevelNavItem(
    contentKey = EXPLORE_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_3_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_3_unselected,
    iconTextId = R.string.explore,
)

val MY_PAGE = TopLevelNavItem(
    contentKey = MYPAGE_MAIN_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_4_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_4_unselected,
    iconTextId = R.string.my_page,
)

val SPLASH = TopLevelNavItem(contentKey = SPLASH_CONTENT_KEY)

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