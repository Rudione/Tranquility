package my.rudione.tranquility.android.di

import my.rudione.home.HomeViewModel
import my.rudione.login.LoginViewModel
import my.rudione.post.PostDetailViewModel
import my.rudione.signup.SignUpViewModel
import my.rudione.tranquility.android.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get()) }
    factory { SignUpViewModel(get()) }
    viewModel { MainViewModel(get()) }
    factory { HomeViewModel() }
    factory { PostDetailViewModel() }
}