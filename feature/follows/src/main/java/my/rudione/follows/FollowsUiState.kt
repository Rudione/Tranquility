package my.rudione.follows

import my.rudione.tranquility.common.domain.model.FollowsUser

data class FollowsUiState(
    val isLoading: Boolean = false,
    val followsUsers: List<FollowsUser> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false
)