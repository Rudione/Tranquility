package my.rudione.common.datastore

import kotlinx.serialization.Serializable
import my.rudione.tranquility.auth.domain.model.AuthResultData

@Serializable
data class UserSettings(
    val id: Int = -1,
    val name: String = "",
    val bio: String = "",
    val avatar: String? = null,
    val token: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)

fun UserSettings.toAuthResultData() = AuthResultData(
    id = id,
    name = name,
    bio = bio,
    avatar = avatar,
    token = token,
    followersCount = followersCount,
    followingCount = followingCount
)

fun AuthResultData. toUserSettings() = UserSettings(
    id = id,
    name = name,
    bio = bio,
    avatar = avatar,
    token = token,
    followersCount = followersCount,
    followingCount = followingCount
)