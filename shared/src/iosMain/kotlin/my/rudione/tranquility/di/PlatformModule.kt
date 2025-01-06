package my.rudione.tranquility.di

import my.rudione.tranquility.common.data.IOSUserPreferences
import my.rudione.tranquility.common.data.local.UserPreferences
import org.koin.dsl.module

actual val platformModule = module {
    single<UserPreferences> { IOSUserPreferences(get()) }

    single {
        //createDatastore()
    }
}