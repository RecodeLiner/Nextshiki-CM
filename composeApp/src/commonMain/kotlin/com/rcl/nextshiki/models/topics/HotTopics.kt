package com.rcl.nextshiki.models.topics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HotTopics(
    @SerialName("id") val id: Int? = null,
    @SerialName("topic_title") val topicTitle: String? = null,
    @SerialName("body") val body: String? = null,
    @SerialName("html_body") val htmlBody: String? = null,
    @SerialName("html_footer") val htmlFooter: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("comments_count") val commentsCount: Int? = null,
    @SerialName("forum") val forum: Forums? = Forums(),
    @SerialName("user") val user: User? = User(),
    @SerialName("type") val topicType: String? = null,
    @SerialName("linked_id") val linkedId: Int? = null,
    @SerialName("linked_type") val linkedType: String? = null,
    @SerialName("linked") val linked: Linked? = Linked(),
    @SerialName("viewed") val viewed: Boolean? = null,
    @SerialName("last_comment_viewed") val lastCommentViewed: Boolean? = null,
    @SerialName("event") val event: String? = null,
    @SerialName("episode") val episode: String? = null
)
