package com.example.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Application.configureRouting() {

    val allUsers = mutableMapOf<String, String>()
    val usersForGameSession = mutableMapOf<String, String>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/api/addUser") {
            val nickName = call.request.queryParameters["user"].toString()
            if (!allUsers.containsKey(nickName)) {
                val id = UUID.randomUUID().toString()
                allUsers[nickName] = id
                call.respondText(id)
            } else {
                call.respondText("Это имя занято")
            }
        }

        get("/api/goPlay") {
            val nickName = call.request.queryParameters["user"].toString()

            if (!usersForGameSession.containsKey(nickName)) {
                usersForGameSession[nickName] = usersForGameSession.size.toString()
                call.respondText("Успех", status = HttpStatusCode.fromValue(45))
            }

            if (usersForGameSession.size == 2) {
                call.respondText("Нанчинаем игру!")
                usersForGameSession.clear()
            } else {
                call.respondText("Ожидаем")
            }
        }

        post("/api/") {
//            call.respondText("!!!!!!!!!")
//            val customer = call.receive<User>()
//            users.add(customer)
            val formParameters = call.receiveParameters()
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
    }
}
