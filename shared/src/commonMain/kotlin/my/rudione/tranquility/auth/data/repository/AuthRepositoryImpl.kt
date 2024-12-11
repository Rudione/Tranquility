package my.rudione.tranquility.auth.data.repository

import kotlinx.coroutines.withContext
import my.rudione.tranquility.auth.data.AuthService
import my.rudione.tranquility.auth.data.mapper.toAuthResultData
import my.rudione.tranquility.auth.data.model.SignInRequest
import my.rudione.tranquility.auth.data.model.SignUpRequest
import my.rudione.tranquility.auth.domain.common.DispatcherProvider
import my.rudione.tranquility.auth.domain.common.Result
import my.rudione.tranquility.auth.domain.model.AuthResultData
import my.rudione.tranquility.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dispatcher: DispatcherProvider,
    private val authService: AuthService
): AuthRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> {
        return withContext(dispatcher.io) {
            try {
                val request = SignUpRequest(name, email, password)

                val response = authService.signUp(request)

                if (response.data == null) {
                    Result.Error(
                        errorMessage = response.errorMessage!!
                    )
                } else {
                    Result.Success(
                        data = response.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResultData> {
        return withContext(dispatcher.io) {
            try {
                val request = SignInRequest(email, password)

                val response = authService.signIn(request)

                if (response.data == null) {
                    Result.Error(
                        errorMessage = response.errorMessage!!
                    )
                } else {
                    Result.Success(
                        data = response.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }
}