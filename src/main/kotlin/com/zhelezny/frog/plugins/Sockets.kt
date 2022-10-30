package com.zhelezny.frog.plugins

import com.zhelezny.frog.Connection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

val savedPlayers = mutableMapOf<String, String>()

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val allUsers = mutableMapOf<String, String>()
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

        webSocket("/getUid") {
            try {
                val msg = incoming.receive() as Frame.Text
//                val user = msg.readText().split(":")
//                val nickName = user[0]
//                if (user.size == 2) {
//                    if (allUsers.containsValue(user[1])) {
//                        send("!GOOD")
//                    } else {
//                        val id = UUID.randomUUID().toString()
//                        allUsers[nickName] = id
//                        send(id)
//                    }
//                } else {
//                    val id = UUID.randomUUID().toString()
//                    allUsers[nickName] = id
//                    send(id)
//                }
                val nickName = msg.readText()
                if (!allUsers.containsKey(nickName)) {
                    val id = UUID.randomUUID().toString()
                    println("Отправляем UID: $id")
                    allUsers[nickName] = id
                    send(id)
                } else {
                    send("Это имя занято")
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Close connect!")
            }
        }

        val userForGameSession = StringBuilder()

        webSocket("/joinGame") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                println("Новое соединение! Всего: ${connections.count()}")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    println("Входящее сообщение: $receivedText")
                    userForGameSession.append("$receivedText:")
                    connections.forEach {
                        println("Отправка сообщения: $userForGameSession")
                        it.session.send(userForGameSession.toString())
                    }
                    if (connections.size == 1) {
                        launch {
                            println("Старт таймер...")
                            repeat(60) {
                                delay(1000L)
                            }
                            println("Истёк таймер")
                            connections.forEach {
                                println("Присваивание цвета: $userForGameSession")
                                it.session.send(userForGameSession.toString())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }

        webSocket("/goPlay2") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }

        webSocket("/chat") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }


//        val nickName = call.request.queryParameters["user"].toString()
//
//        if (!usersForGameSession.containsKey(nickName)) {
//            usersForGameSession[nickName] = usersForGameSession.size.toString()
//        }
//
//        if (usersForGameSession.size == 2) {
//            call.respondText("Нанчинаем игру!", status = HttpStatusCode.Accepted)
//            usersForGameSession.clear()
//        } else {
//            call.respondText("Ожидаем")
//        }


    }
}
