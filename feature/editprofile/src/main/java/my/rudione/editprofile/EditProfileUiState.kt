package my.rudione.editprofile

import my.rudione.common.fake_data.Profile

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val uploadSucceed: Boolean = false,
    val errorMessage: String? = null
)