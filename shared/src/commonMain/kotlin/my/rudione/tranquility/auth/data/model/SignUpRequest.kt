package my.rudione.tranquility.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)