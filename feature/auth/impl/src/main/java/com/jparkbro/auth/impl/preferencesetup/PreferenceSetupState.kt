package com.jparkbro.auth.impl.preferencesetup

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.Anime
import com.jparkbro.core.model.Cursor
import com.jparkbro.core.model.Genre
import com.jparkbro.core.model.Season

data class PreferenceSetupState(
    val searchFieldState: TextFieldState = TextFieldState(),
    val selectedYear: Int? = null,
    val selectedSeason: Season? = null,
    val selectedGenre: Genre? = null,
    val animeList: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val ratings: List<AnimeRating> = emptyList(),
    val isSearchLoading: Boolean = false,
    val isCompleting: Boolean = false,
) {
    val ratedCount: Int get() = ratings.size
    val isCompleteEnabled: Boolean get() = ratings.isNotEmpty()
}

/** 평가 완료 시 서버에 `List<{ animeId, rating }>`로 그대로 전송할 항목. */
data class AnimeRating(
    val animeId: Long,
    val rating: Float,
)
