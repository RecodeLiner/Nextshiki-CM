package com.rcl.nextshiki.components.newscomponent.mainpage.subelements

import kotlinx.serialization.Serializable

@Serializable
data class CardElement(
    val id: Int = 0,
    val name: String = "",
    val russian: String = "",
    val imageLink: String = "",
    val nextEpisodeAt: String = ""
)
