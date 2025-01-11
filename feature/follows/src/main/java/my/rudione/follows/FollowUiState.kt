package my.rudione.follows

import my.rudione.common.fake_data.SampleFollowsUser

data class FollowUiState(
    val isLoading: Boolean = false,
    val sampleFollowsUsers: List<SampleFollowsUser> = listOf(),
    val errorMessage: String? = null
)