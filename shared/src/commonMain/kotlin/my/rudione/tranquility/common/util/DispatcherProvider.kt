package my.rudione.tranquility.common.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}

internal expect fun provideDispatcher(): DispatcherProvider