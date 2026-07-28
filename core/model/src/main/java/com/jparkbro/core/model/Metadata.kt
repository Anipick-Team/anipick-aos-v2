package com.jparkbro.core.model

data class Metadata(
    val seasonYears: List<Int> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val types: List<String> = emptyList(),
)
