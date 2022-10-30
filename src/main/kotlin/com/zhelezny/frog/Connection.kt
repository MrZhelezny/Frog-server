package com.zhelezny.frog

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: WebSocketSession) {
    companion object {
        var sessionId = AtomicInteger(0)
    }
    val name = "user${sessionId.getAndIncrement()}"
}