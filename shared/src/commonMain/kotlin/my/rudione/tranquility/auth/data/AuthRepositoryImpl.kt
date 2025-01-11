package my.rudione.tranquility.auth.data

import kotlinx.coroutines.withContext
import my.rudione.tranquility.auth.domain.model.AuthResultData
import my.rudione.tranquility.auth.domain.repository.AuthRepository
import my.rudione.tranquility.common.data.local.UserPreferences
import my.rudione.tranquility.common.data.local.toUserSettings
import my.rudione.tranquility.common.util.DispatcherProvider
import my.rudione.tranquility.common.util.Result

internal class AuthRepositoryImpl(
    private val dispatcher: DispatcherProvider,
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> {
        return withContext(dispatcher.io) {
            try {
                val request = SignUpRequest(name, email, password)

                val authResponse = authService.signUp(request)

                if (authResponse.data == null) {
                    Result.Error(
                        message = authResponse.errorMessage!!
                    )
                } else {
                    userPreferences.setUserData(
                        authResponse.data.toAuthResultData().toUserSettings()
                    )
                    Result.Success(
                        data = authResponse.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    message = "Oops, we could not send your request, try later!"
                )
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResultData> {
        return withContext(dispatcher.io) {
            try {
                val request = SignInRequest(email, password)

                val authResponse = authService.signIn(request)

                if (authResponse.data == null) {
                    Result.Error(
                        message = authResponse.errorMessage!!
                    )
                } else {
                    userPreferences.setUserData(
                        authResponse.data.toAuthResultData().toUserSettings()
                    )
                    Result.Success(
                        data = authResponse.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    message = "Oops, we could not send your request, try later!"
                )
            }
        }
    }
}