package com.jparkbro.auth.impl.preferencesetup

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeRating
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Metadata
import com.jparkbro.core.model.metadata.Season
import com.jparkbro.core.model.pagination.Cursor

data class PreferenceSetupState(
    val searchFieldState: TextFieldState = TextFieldState(),
    val metadata: Metadata = Metadata(),
    val activeFilterSheet: FilterType? = null,
    val selectedYear: Int? = null,
    val selectedSeason: Season? = null,
    val selectedGenre: Genre? = null,
    val animeList: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val ratings: List<AnimeRating> = emptyList(),
    val isSearchLoading: Boolean = false,
    val isSearchError: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val isCompleting: Boolean = false,
) {
    val ratedCount: Int get() = ratings.size
    val isCompleteEnabled: Boolean get() = ratings.isNotEmpty()
}
