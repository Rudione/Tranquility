package my.rudione.common.fake_data

data class FollowsUser(
    val id: Long,
    val name: String,
    val bio: String = "Hey there, welcome to my profile!",
    val profileUrl: String,
    val isFollowing: Boolean = false
)

val sampleUsers = listOf(
    FollowsUser(
        id = 1,
        name = "Denis Rudenko",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 2,
        name = "John Cena",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 3,
        name = "Cristiano",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 4,
        name = "L. James",
        profileUrl = "https://picsum.photos/200"
    )
)