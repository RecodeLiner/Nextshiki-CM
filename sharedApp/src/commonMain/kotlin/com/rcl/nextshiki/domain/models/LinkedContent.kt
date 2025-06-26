package com.rcl.nextshiki.domain.models

import kotlinx.datetime.LocalDate

data class LinkedContent(
    val id: Int?,
    val name: String?,
    val russianName: String?,
    val imageUrls: ImageUrls?,
    val url: String?,
    val kind: String?,
    val score: Float?,
    val status: String?,
    val episodes: Int?,
    val episodesAired: Int?,
    val airedOn: LocalDate?,
    val releasedOn: LocalDate?
)