package my.rudione.tranquility.di

import my.rudione.tranquility.auth.data.AuthRepositoryImpl
import my.rudione.tranquility.auth.data.AuthService
import my.rudione.tranquility.auth.domain.common.provideDispatcher
import my.rudione.tranquility.auth.domain.repository.AuthRepository
import my.rudione.tranquility.auth.domain.usecase.SignInUseCase
import my.rudione.tranquility.auth.domain.usecase.SignUpUseCase
import org.koin.dsl.module

private val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInUseCase() }

}

private val utilityModule = module {
    factory { provideDispatcher() }
}

fun getSharedModules() = listOf(authModule, utilityModule)