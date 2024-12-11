package my.rudione.tranquility.auth.domain.common

sealed class Result<T>(
    data: T? = null,
    errorMessage: String? = null
) {
    class Success<T>(
        val data: T
    ) : Result<T>(data = data)

    class Error<T>(
        val data: T? = null,
        val errorMessage: String
    ) : Result<T>(data = data, errorMessage = errorMessage)
}