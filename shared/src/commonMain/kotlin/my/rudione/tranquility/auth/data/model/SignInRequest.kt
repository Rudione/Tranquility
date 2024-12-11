package my.rudione.tranquility.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SignInRequest(
    val email: String,
    val password: String
)