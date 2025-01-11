package my.rudione.profile

import my.rudione.common.fake_data.SamplePost
import my.rudione.common.fake_data.Profile

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    var errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = true,
    val samplePosts: List<SamplePost> = emptyList(),
    var errorMessage: String? = null
)