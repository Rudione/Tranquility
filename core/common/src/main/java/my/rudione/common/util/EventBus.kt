package my.rudione.common.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import my.rudione.tranquility.common.domain.model.Post

object EventBus {
    private val _events = MutableSharedFlow<Event>(
        extraBufferCapacity = Constants.EVENT_BUS_BUFFER_CAPACITY
    )
    val events = _events.asSharedFlow()

    suspend fun send(event: Event) {
        _events.emit(event)
    }
}

sealed interface Event {
    data class PostUpdated(val post: Post): Event
}