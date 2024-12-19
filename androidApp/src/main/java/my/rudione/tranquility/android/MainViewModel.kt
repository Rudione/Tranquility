package my.rudione.tranquility.android

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import my.rudione.common.datastore.UserSettings
import my.rudione.common.datastore.toAuthResultData

class MainViewModel(
    dataStore: DataStore<UserSettings>
) : ViewModel() {

    val authState = dataStore.data.map {
        it.toAuthResultData().token
    }
}