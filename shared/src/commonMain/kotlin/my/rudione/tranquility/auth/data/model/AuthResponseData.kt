package my.rudione.tranquility.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthResponseData(
    val id: Int,
    val name: String,
    val bio: String,
    val avatar: String? = null,
    val token: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)