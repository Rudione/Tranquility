package my.rudione.common.fake_data

import my.rudione.tranquility.common.domain.model.FollowsUser

data class SampleFollowsUser(
    val id: Long,
    val name: String,
    val bio: String = "Hey there, welcome to my profile!",
    val profileUrl: String,
    val isFollowing: Boolean = false
) {
    fun toFollowsUser() : FollowsUser {
        return FollowsUser(
            id = id,
            name = name,
            bio = bio,
            imageUrl = profileUrl,
            isFollowing = isFollowing
        )
    }
}

val sampleUsers = listOf(
    SampleFollowsUser(
        id = 1,
        name = "Denis Rudenko",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 2,
        name = "John Cena",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 3,
        name = "Cristiano",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 4,
        name = "L. James",
        profileUrl = "https://picsum.photos/200"
    )
)