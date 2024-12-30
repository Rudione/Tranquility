package my.rudione.tranquility.android.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import my.rudione.common.datastore.UserSettings
import my.rudione.common.datastore.UserSettingsSerializer
import my.rudione.home.HomeViewModel
import my.rudione.login.LoginViewModel
import my.rudione.signup.SignUpViewModel
import my.rudione.tranquility.android.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { LoginViewModel(get(), get()) }
    factory { SignUpViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    factory { HomeViewModel() }

    single<DataStore<UserSettings>> {
        DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            produceFile = {
                androidContext().dataStoreFile("app_user_settings")
            }
        )
    }
}