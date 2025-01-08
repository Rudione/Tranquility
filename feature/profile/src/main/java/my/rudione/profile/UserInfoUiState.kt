package my.rudione.profile

import my.rudione.common.fake_data.Post
import my.rudione.common.fake_data.Profile

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    var errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = true,
    val posts: List<Post> = emptyList(),
    var errorMessage: String? = null
)