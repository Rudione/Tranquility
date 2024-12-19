package my.rudione.signup

data class SignUpUiState(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var isAuthenticating : Boolean = false,
    var authErrorMessage : String? = null,
    var isAuthSuccess : Boolean = false
)