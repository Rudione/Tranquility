package my.rudione.tranquility.android.di

import my.rudione.login.LoginViewModel
import my.rudione.signup.SignUpViewModel
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel() }
    factory { SignUpViewModel() }
}
