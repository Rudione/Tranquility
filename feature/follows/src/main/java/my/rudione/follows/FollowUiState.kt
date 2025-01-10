package my.rudione.follows

import my.rudione.common.fake_data.FollowsUser

data class FollowUiState(
    val isLoading: Boolean = false,
    val followsUsers: List<FollowsUser> = listOf(),
    val errorMessage: String? = null
)