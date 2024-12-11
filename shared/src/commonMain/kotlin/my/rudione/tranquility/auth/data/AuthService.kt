package my.rudione.tranquility.auth.data

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import my.rudione.tranquility.auth.data.model.AuthResponse
import my.rudione.tranquility.auth.data.model.SignInRequest
import my.rudione.tranquility.auth.data.model.SignUpRequest

class AuthService: KtorApi() {
    internal suspend fun signUp(signUpRequest: SignUpRequest): AuthResponse = client.post {
        endpoint(path = "signup")
        setBody(signUpRequest)
    }.body()

    internal suspend fun signIn(signInRequest: SignInRequest): AuthResponse = client.post {
        endpoint(path = "login")
        setBody(signInRequest)
    }.body()


}