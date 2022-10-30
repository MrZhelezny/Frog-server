package com.zhelezny.frog

import com.typesafe.config.ConfigFactory
import com.zhelezny.frog.plugins.configureRouting
import com.zhelezny.frog.plugins.configureSockets
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.config.*

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        module {
            configureRouting()
            configureSockets()
        }

        connector {
            port = config.property("ktor.deployment.port").getString().toInt()
            host = config.property("ktor.deployment.host").getString()
        }
    }).start(wait = true)
}
