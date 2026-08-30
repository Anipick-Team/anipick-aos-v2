package com.jparkbro.anipick.navigation

import androidx.annotation.DrawableRes
import com.jparkbro.auth.api.AuthNavKey
import com.jparkbro.auth.impl.navigation.LOGIN_CONTENT_KEY
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
    val label: String = "",
)

val HOME = TopLevelNavItem(
    contentKey = HOME_MAIN_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_1_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_1_unselected,
    label = "홈",
)

val RANKING = TopLevelNavItem(
    contentKey = RANKING_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_2_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_2_unselected,
    label = "랭킹",
)

val EXPLORE = TopLevelNavItem(
    contentKey = EXPLORE_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_3_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_3_unselected,
    label = "탐색",
)

val MY_PAGE = TopLevelNavItem(
    contentKey = MYPAGE_MAIN_CONTENT_KEY,
    selectedIcon = R.drawable.ic_bottom_nav_4_selected,
    unselectedIcon = R.drawable.ic_bottom_nav_4_unselected,
    label = "마이",
)

val SPLASH = TopLevelNavItem(contentKey = SPLASH_CONTENT_KEY)

/** 로그인 플로우(Login/EmailLogin/PasswordReset/PreferenceSetup 등) 전용 최상위 슬롯 - [SPLASH]와
 *  마찬가지로 바텀 네비에는 안 나오지만(BOTTOM_NAV_ITEMS에는 없음) 자기 서브스택을 따로 갖는다.
 *  없으면 [com.jparkbro.auth.api.navigateToLogin]이 "그 순간 활성화돼있던
 *  탭"의 서브스택을 지우고 그 자리에 로그인 화면을 밀어넣는데, 로그인 완료 후엔 Home 탭
 *  서브스택만 정리되니 원래 활성 탭이 Home이 아니었으면 그 탭에 로그인 화면이 계속 남는다. */
val AUTH = TopLevelNavItem(contentKey = LOGIN_CONTENT_KEY)

val TOP_LEVEL_ITEMS = mapOf(
    SplashNavKey.Splash to SPLASH,
    AuthNavKey.Login to AUTH,
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