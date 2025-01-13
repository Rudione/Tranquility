package my.rudione.profile

import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.common.domain.model.Post

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    var errorMessage: String? = null,
    val endReached: Boolean = false
)