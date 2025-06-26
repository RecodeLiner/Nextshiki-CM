
import com.rcl.nextshiki.data.dto.ForumDto
import com.rcl.nextshiki.data.dto.LinkedAnimeDto
import com.rcl.nextshiki.data.dto.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicResponse(
    @SerialName("id") val id: Int?,
    @SerialName("topic_title") val topicTitle: String?,
    @SerialName("body") val body: String?,
    @SerialName("html_body") val htmlBody: String?,
    @SerialName("html_footer") val htmlFooter: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("comments_count") val commentsCount: Int?,
    @SerialName("forum") val forum: ForumDto?,
    @SerialName("user") val user: UserDto?,
    @SerialName("type") val type: String?,
    @SerialName("linked_id") val linkedId: Int?,
    @SerialName("linked_type") val linkedType: String?,
    @SerialName("linked") val linked: LinkedAnimeDto?,
    @SerialName("viewed") val viewed: Boolean?,
    @SerialName("last_comment_viewed") val lastCommentViewed: String?,
    @SerialName("event") val event: String?,
    @SerialName("episode") val episode: Int?
)