package com.jparkbro.home.impl.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.home.impl.components.DayOfWeekSelector
import com.jparkbro.home.impl.main.HomeMainAction
import com.jparkbro.home.impl.main.HomeMainState

/** 홈 메인의 섹션들 - MainScreen LazyColumn의 item들. */
internal fun LazyListScope.homeMainSections(
    state: HomeMainState,
    onAction: (HomeMainAction) -> Unit,
) {
    // 실시간 인기 ( Trending Animes )
    if (state.trendingAnimes.isNotEmpty()) {
        item {
            HomeSection(
                title = "실시간 인기 애니메이션",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnTrendingMoreClick) },
            ) {
                AnimeCardRow(
                    animes = state.trendingAnimes,
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }

    // 추천 ( 평가기반 Recommendation Animes )
    item {
        if (state.recommendation.animes.isNullOrEmpty()) {
            EmptyRecommendationImage(imageRes = R.drawable.empty_recommend_image)
        } else {
            HomeSection(
                title = {
                    RecommendationSectionTitle(
                        nickname = state.nickname,
                        referenceAnimeTitle = state.recommendation.referenceAnimeTitle,
                    )
                },
                moreContentDescription = "오늘의 추천작 더보기",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnRecommendationMoreClick) },
            ) {
                AnimeCardRow(
                    animes = state.recommendation.animes ?: emptyList(),
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }

    // 요일별 신작 ( Weekly Animes )
    if (state.weeklyAnimes.isNotEmpty()) {
        item {
            HomeSection(
                title = "요일별 신작",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnWeeklyMoreClick) },
                subContent = {
                    DayOfWeekSelector(
                        selectedDay = state.selectedDayOfWeek,
                        onDaySelected = { day -> onAction(HomeMainAction.OnDaySelected(day)) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                },
            ) {
                AnimeCardRow(
                    animes = state.weeklyAnimes,
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }

    // 최근 리뷰
    if (state.recentReviews.isNotEmpty()) {
        item {
            HomeSection(
                title = "최근 리뷰",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnRecentReviewMoreClick) },
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.recentReviews) { review ->
                        RecentReviewCard(
                            review = review
                        )
                    }
                }
            }
        }
    }

    // 방영 예정 ( Upcoming Animes )
    if (!state.upcomingSeason.animes.isNullOrEmpty()) {
        item {
            val seasonYear = state.upcomingSeason.seasonYear
            val season = state.upcomingSeason.season
            HomeSection(
                title = {
                    Row {
                        if (seasonYear != null && season != null) {
                            Text(
                                text = "${seasonYear}년 ${season}분기 ",
                                style = AniPickTheme.typography.h3,
                                color = AniPickTheme.colors.primary,
                            )
                        }
                        Text(
                            text = "방영예정",
                            style = AniPickTheme.typography.h3,
                            color = AniPickTheme.colors.black,
                        )
                    }
                },
                moreContentDescription = "방영예정 더보기",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnUpcomingSeasonMoreClick) },
            ) {
                AnimeCardRow(
                    animes = state.upcomingSeason.animes ?: emptyList(),
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }

    // 추천 애니 ( 최근 확인 애니 기반 Similar Animes )
    item {
        if (state.recentAnimeRecommendation.animes.isNullOrEmpty()) {
            EmptyRecommendationImage(imageRes = R.drawable.empty_similar_image)
        } else {
            HomeSection(
                title = {
                    SimilarRecommendationSectionTitle(
                        referenceAnimeTitle = state.recentAnimeRecommendation.referenceAnimeTitle,
                    )
                },
                moreContentDescription = "최근 확인한 애니 기반 추천 더보기",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnRecentAnimeRecommendationMoreClick) },
            ) {
                AnimeCardRow(
                    animes = state.recentAnimeRecommendation.animes ?: emptyList(),
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }

    // 공개 예정 ( Coming Soon Animes )
    if (state.comingSoonAnimes.isNotEmpty()) {
        item {
            HomeSection(
                title = "공개 예정",
                titlePadding = PaddingValues(horizontal = 20.dp),
                onMoreClick = { onAction(HomeMainAction.OnComingSoonMoreClick) },
            ) {
                AnimeCardRow(
                    animes = state.comingSoonAnimes,
                    onAnimeClick = { onAction(HomeMainAction.OnAnimeClick(it)) },
                )
            }
        }
    }
}
