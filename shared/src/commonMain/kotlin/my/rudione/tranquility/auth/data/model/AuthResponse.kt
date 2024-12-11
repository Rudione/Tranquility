package my.rudione.tranquility.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthResponse(
    val data: AuthResponseData? = null,
    val errorMessage: String? = null
)