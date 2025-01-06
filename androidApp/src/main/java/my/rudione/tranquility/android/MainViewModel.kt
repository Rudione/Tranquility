package my.rudione.tranquility.android

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import my.rudione.tranquility.common.data.local.UserSettings
import my.rudione.tranquility.common.data.local.toAuthResultData

class MainViewModel(
    dataStore: DataStore<UserSettings>
) : ViewModel() {

    val authState = dataStore.data.map {
        it.toAuthResultData().token
    }
}