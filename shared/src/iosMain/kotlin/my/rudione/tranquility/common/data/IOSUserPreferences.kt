package my.rudione.tranquility.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import my.rudione.tranquility.common.data.local.UserPreferences
import my.rudione.tranquility.common.data.local.UserSettings


internal class IOSUserPreferences(
    private val dataStore: DataStore<Preferences>
): UserPreferences{
    override suspend fun getUserData(): UserSettings {
        TODO("Not yet implemented")
    }

    override suspend fun setUserData(userSettings: UserSettings) {
        TODO("Not yet implemented")
    }
}

/*
@OptIn(ExperimentalForeignApi::class)
internal fun createDatastore(): DataStore<Preferences>{
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/$PREFERENCES_FILE_NAME").toPath()
        }
    )
} */