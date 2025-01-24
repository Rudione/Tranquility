package my.rudione.editprofile

import my.rudione.tranquility.account.domain.model.Profile

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val uploadSucceed: Boolean = false,
    val errorMessage: String? = null
)