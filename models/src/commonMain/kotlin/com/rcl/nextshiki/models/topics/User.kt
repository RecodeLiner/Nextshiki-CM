package com.rcl.nextshiki.models.topics

import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.universal.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") override val id: Int? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("avatar") val avatar: String? = null,
    @SerialName("image") override val image: Image? = Image(),
    @SerialName("last_online_at") val lastOnlineAt: String? = null,
    @SerialName("url") override val url: String? = null
) : CommonSearchInterface {
    override val name: String?
        get() = nickname
    override val russian: String?
        get() = nickname
    override val searchType: SearchType
        get() = SearchType.Users
}
