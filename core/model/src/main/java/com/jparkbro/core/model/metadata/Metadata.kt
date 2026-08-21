package com.jparkbro.core.model.metadata

data class Metadata(
    val seasonYears: List<Int>? = null,
    val seasons: List<Season>? = null,
    val genres: List<Genre>? = null,
    val types: List<String>? = null,
)
