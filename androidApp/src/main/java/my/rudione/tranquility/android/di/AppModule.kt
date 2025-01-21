package my.rudione.tranquility.android.di

import my.rudione.editprofile.EditProfileViewModel
import my.rudione.home.HomeViewModel
import my.rudione.login.LoginViewModel
import my.rudione.post.PostDetailViewModel
import my.rudione.profile.ProfileViewModel
import my.rudione.signup.SignUpViewModel
import my.rudione.tranquility.android.MainViewModel
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get()) }
    factory { SignUpViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { PostDetailViewModel(get(), get(), get(), get(), get()) }
    factory { ProfileViewModel(get(), get(), get(), get()) }
    factory { EditProfileViewModel() }
}