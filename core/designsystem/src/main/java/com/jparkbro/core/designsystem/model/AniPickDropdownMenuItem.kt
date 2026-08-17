package com.jparkbro.core.designsystem.model

import androidx.compose.runtime.Composable

data class AniPickDropdownMenuItem(
    val content: @Composable () -> Unit,
    val onClick: () -> Unit,
)
