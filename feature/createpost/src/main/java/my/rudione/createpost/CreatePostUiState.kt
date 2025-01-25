package my.rudione.createpost

data class CreatePostUiState(
    val caption: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val postCreated: Boolean = false
)