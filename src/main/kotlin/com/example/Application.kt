package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.features.*

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        module {
            install(DoubleReceive)
            configureRouting()
            configureSerialization()
        }

        connector {
            port = config.property("ktor.deployment.port").getString().toInt()
            host = config.property("ktor.deployment.host").getString()
        }
    }).start(wait = true)
}
