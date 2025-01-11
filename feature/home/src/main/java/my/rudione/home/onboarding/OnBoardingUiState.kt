package my.rudione.home.onboarding

import my.rudione.tranquility.common.domain.model.FollowsUser

data class OnBoardingUiState(
    val isLoading: Boolean = false,
    val users: List<FollowsUser> = listOf(),
    val errorMessage: String? = null,
    val shouldShowOnBoarding: Boolean = true
)