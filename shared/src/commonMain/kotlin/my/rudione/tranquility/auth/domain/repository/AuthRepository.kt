package my.rudione.tranquility.auth.domain.repository

import my.rudione.tranquility.auth.domain.model.AuthResultData
import my.rudione.tranquility.common.util.Result

internal interface AuthRepository {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData>

    suspend fun signIn(email: String, password: String): Result<AuthResultData>
}
