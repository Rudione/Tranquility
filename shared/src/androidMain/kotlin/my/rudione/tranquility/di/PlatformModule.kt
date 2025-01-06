package my.rudione.tranquility.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import my.rudione.tranquility.common.data.AndroidUserPreferences
import my.rudione.tranquility.common.data.UserSettingsSerializer
import my.rudione.tranquility.common.data.local.PREFERENCES_FILE_NAME
import my.rudione.tranquility.common.data.local.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<UserPreferences> { AndroidUserPreferences(get()) }

    single {
        DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            produceFile = {
                androidContext().dataStoreFile(
                    fileName = PREFERENCES_FILE_NAME
                )
            }
        )
    }
}