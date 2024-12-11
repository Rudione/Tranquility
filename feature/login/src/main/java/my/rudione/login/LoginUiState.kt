package my.rudione.login

data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var isAuthenticating : Boolean = false,
    var authErrorMessage : String? = null,
    var isAuthSuccess : Boolean = false
)
