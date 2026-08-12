package com.jparkbro.home.impl.main.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.AniPickAnimeCard
import com.jparkbro.core.ui.objectParticleFor

/**
 * "오늘의 추천작" 섹션 제목. [referenceAnimeTitle]이 있으면(특정 애니를 재밌게 봤다는 게 있으면)
 * 그 작품 기준 문구를, 없으면 [nickname] 기준의 기본 문구를 보여준다.
 */
@Composable
internal fun RecommendationSectionTitle(
    nickname: String?,
    referenceAnimeTitle: String?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (referenceAnimeTitle != null) {
            Row {
                Text(
                    text = referenceAnimeTitle,
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = "${objectParticleFor(referenceAnimeTitle)} 재밌게 보셨다면,",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.black,
                )
            }
            Text(
                text = "이 작품들도 마음에 드실 거에요!",
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
        } else {
            Row {
                Text(
                    text = "오늘의 추천작, ",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.black,
                )
                Text(
                    text = nickname.orEmpty(),
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = " 님의",
                    style = AniPickTheme.typography.h3,
                    color = AniPickTheme.colors.black,
                )
            }
            Text(
                text = "취향에 맞춰 준비했어요!",
                style = AniPickTheme.typography.h3,
                color = AniPickTheme.colors.black,
            )
        }
    }
}

/** 홈 화면 애니 목록 섹션들이 공통으로 쓰는 가로 스크롤 카드 목록. */
@Composable
internal fun AnimeCardRow(
    animes: List<Anime>,
    onAnimeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(animes) { anime ->
            AniPickAnimeCard(
                anime = anime,
                onClick = { anime.animeId?.let(onAnimeClick) },
            )
        }
    }
}

/**
 * 추천 애니 목록이 비었을 때 [HomeSection]째로 대신 넣는 안내 이미지
 */
@Composable
internal fun EmptyRecommendationImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "추천 애니 목록이 비었을 때 안내 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 362.dp)
        )
    }
}

@Composable
internal fun RecentReviewCard(
    title: String,
    content: String,
    nickname: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(228.dp)
            .background(AniPickTheme.colors.white, RoundedCornerShape(8.dp))
            .border(1.dp, AniPickTheme.colors.gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = title,
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.textGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = AniPickTheme.typography.body2,
            color = AniPickTheme.colors.black,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nickname,
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            VerticalDivider(
                modifier = Modifier.height(10.dp),
                thickness = 1.dp,
                color = AniPickTheme.colors.textGray
            )
            Text(
                text = date,
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 600)
private fun EmptyRecommendationImagePreview() {
    EmptyRecommendationImage(imageRes = R.drawable.empty_recommend_image)
}

@Composable
@Preview(showBackground = true)
private fun RecentReviewCardPreview() {
    RecentReviewCard(
        title = "title",
        content = "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent",
        nickname = "nicknamenicknamenicknamenicknamenicknamenicknamenicknamenickname",
        date = "2026.08.01",
    )
}
