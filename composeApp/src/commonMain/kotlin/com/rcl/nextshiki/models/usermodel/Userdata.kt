package com.rcl.nextshiki.models.usermodel

import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Userdata (
    @SerialName("id") val id: Int? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("avatar") val avatar: String? = null,
    @SerialName("image") val image: Image? = Image(),
    @SerialName("last_online_at") val lastOnlineAt: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("sex") val sex: String? = null,
    @SerialName("full_years") val fullYears: String? = null,
    @SerialName("last_online") val lastOnline: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("banned") val banned: Boolean?= null,
    @SerialName("about") val about: String? = null,
    @SerialName("about_html") val aboutHtml: String? = null,
    @SerialName("common_info") val commonInfo: List<String> = arrayListOf(),
    @SerialName("show_comments") val showComments: Boolean?= null,
    @SerialName("in_friends") val inFriends: Boolean? = null,
    @SerialName("is_ignored") val isIgnored: Boolean?= null,
    @SerialName("stats") val stats: Stats? = Stats(),
    @SerialName("style_id") val styleId: Int? = null
)