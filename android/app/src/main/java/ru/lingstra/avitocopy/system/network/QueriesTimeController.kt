package ru.lingstra.avitocopy.system.network

import io.reactivex.Completable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class QueriesTimeController @Inject constructor() {

    private val queriesPerSecond: Int = 3
    private val queue: Queue<Long> = Queue(3)

    fun nextQuery(): Completable {
        val time = Date().time
        if (queue.size < queriesPerSecond) {
            queue.put(time)
            return Completable.complete()
        }
        if (Date().time - queue.firstOrDefault(0) > 1001L) {
            queue.put(time)
            return Completable.complete()
        }
        val delay = time - queue.firstOrDefault(0)
        assert(delay > 0)
        queue.put(time)
        return Completable.complete().delay(delay, TimeUnit.MILLISECONDS)
    }
}