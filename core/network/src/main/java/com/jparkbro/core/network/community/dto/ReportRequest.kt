package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    val targetType: String,
    val targetId: Long,
    val reportCategory: String,
)
