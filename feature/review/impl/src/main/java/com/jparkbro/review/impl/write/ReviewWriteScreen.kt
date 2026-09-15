package com.jparkbro.review.impl.write

import android.content.Intent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickBaseTextField
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickRatingBox
import com.jparkbro.core.designsystem.component.AniPickShimmerBox
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.component.AniPickTopBarSubmitAction
import com.jparkbro.core.designsystem.model.ButtonSize
import com.jparkbro.core.designsystem.model.TextFieldType
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.ui.effect.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ReviewWriteRoot(
    animeId: Long,
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    viewModel: ReviewWriteViewModel = koinViewModel(parameters = { parametersOf(animeId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ReviewWriteEvent.SubmitSuccess -> onSubmitSuccess()
        }
    }

    ReviewWriteScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ReviewWriteAction.Navigation -> when (action) {
                    ReviewWriteAction.OnBackClick -> onBackClick()
                    ReviewWriteAction.OnGuidelineClick -> {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://anipick.p-e.kr/community-guidelines.html".toUri()
                        )
                        context.startActivity(intent)
                    }
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun ReviewWriteScreen(
    state: ReviewWriteState,
    onAction: (ReviewWriteAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        topBar = {
            AniPickTitleTopAppBar(
                onBackClick = { onAction(ReviewWriteAction.OnBackClick) },
                titleContent = if (state.isLoading) {
                    { AniPickShimmerBox(modifier = Modifier.size(width = 80.dp, height = 20.dp)) }
                } else {
                    null
                },
                title = if (state.isEditMode) "리뷰 수정" else "리뷰 작성",
                actions = {
                    if (state.isLoading) {
                        AniPickShimmerBox(modifier = Modifier.size(width = 48.dp, height = 32.dp))
                    } else {
                        AniPickTopBarSubmitAction(
                            text = if (state.isEditMode) "수정" else "등록",
                            enabled = state.isSubmitEnabled,
                            onClick = { onAction(ReviewWriteAction.OnSubmitClick) },
                        )
                    }
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
            item {
                AniPickRatingBox(
                    rating = state.rating,
                    onRatingChange = { onAction(ReviewWriteAction.OnRatingChanged(it)) },
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "스포일러",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.primary,
                    )
                    AniPickSwitch(
                        checked = state.isSpoiler,
                        onCheckedChange = { onAction(ReviewWriteAction.OnSpoilerToggle(it)) },
                    )
                }
            }
            item {
                Box {
                    AniPickBaseTextField(
                        state = state.contentState,
                        type = TextFieldType.TEXT,
                        placeholder = "리뷰 내용을 입력해주세요.",
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 6, maxHeightInLines = 10),
                        maxLength = REVIEW_CONTENT_MAX_LENGTH,
                        height = 220.dp,
                        verticalAlignment = Alignment.Top,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 16.dp + 20.dp + 8.dp,
                        ),
                    )
                    Text(
                        text = "${state.contentState.text.length}/$REVIEW_CONTENT_MAX_LENGTH",
                        style = AniPickTheme.typography.caption2,
                        color = AniPickTheme.colors.textGray,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                    )
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "주의사항",
                        style = AniPickTheme.typography.body2,
                        color = AniPickTheme.colors.textGray,
                    )
                    Text(
                        text = "커뮤니티 가이드라인 위반 시 게시물이 삭제되며 서비스 이용이 일정기간 제한되거나 영구적으로 제한될 수 있습니다.\n" +
                                "• 악의적인 욕설, 비방, 혐오 표현 등 타인에게 불쾌감을 줄 수 있는 내용\n" +
                                "• 스포일러 체크 없이 스포일러를 포함한 리뷰\n  (※ 에피소드 내용 요약, 결말 노충 등)\n" +
                                "• 광고, 홍보, 도배 등 리뷰 목적과 무관한 내용\n" +
                                "• 음란물, 성적 수치심을 유발하는 내용\n" +
                                "• 기타 커뮤니티 가이드라인 운영 정책에 위반되는 내용",
                        style = AniPickTheme.typography.caption2,
                        color = AniPickTheme.colors.textGray,
                    )
                    AniPickButton(
                        text = "커뮤니티 가이드라인 전체보기",
                        onClick = { onAction(ReviewWriteAction.OnGuidelineClick) },
                        size = ButtonSize.S,
                        backgroundColor = AniPickTheme.colors.textGray,
                        contentColor = AniPickTheme.colors.white,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ReviewWriteScreenPreview() {
    ReviewWriteScreen(
        state = ReviewWriteState(animeId = 1L, animeTitle = "샘플 애니메이션", animeCoverImageUrl = ""),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun ReviewWriteScreenEditModePreview() {
    ReviewWriteScreen(
        state = ReviewWriteState(
            animeId = 1L,
            animeTitle = "샘플 애니메이션",
            animeCoverImageUrl = "",
            currentReview = Review(reviewId = 1L, rating = 4.5f, content = "이 작품은 정말 인상 깊었습니다."),
            rating = 4.5f,
        ),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun ReviewWriteScreenLoadingPreview() {
    ReviewWriteScreen(
        state = ReviewWriteState(animeId = 1L, animeTitle = "샘플 애니메이션", animeCoverImageUrl = "", isLoading = true),
        onAction = {},
    )
}
