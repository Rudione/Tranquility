package my.rudione.tranquility.android.di

import com.google.ai.client.generativeai.GenerativeModel
import my.rudione.common.util.ImageBytesReader
import my.rudione.createpost.CreatePostViewModel
import my.rudione.editprofile.EditProfileViewModel
import my.rudione.follows.FollowsViewModel
import my.rudione.home.HomeViewModel
import my.rudione.login.LoginViewModel
import my.rudione.post.PostDetailViewModel
import my.rudione.profile.ProfileViewModel
import my.rudione.signup.SignUpViewModel
import my.rudione.tranquility.android.BuildConfig
import my.rudione.tranquility.android.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get()) }
    factory { SignUpViewModel(get()) }
    factory { MainViewModel(get()) }
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { PostDetailViewModel(get(), get(), get(), get(), get()) }
    factory { ProfileViewModel(get(), get(), get(), get()) }
    factory { EditProfileViewModel(get(), get(), get()) }
    factory { FollowsViewModel(get()) }
    factory { CreatePostViewModel(get(), get(), get()) }
    single { ImageBytesReader(androidContext()) }

    single {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.API_KEY
        )
    }
}