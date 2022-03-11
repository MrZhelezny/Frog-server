package com.example.plugins

import com.example.model.User
import com.example.model.UserStatus
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    val users = mutableListOf<User>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/api/") {
            call.respondText("Hello Platon!")
            users.add(User(call.request.queryParameters["user"].toString(), UserStatus.ONLINE))
            if (users.size == 2) {
                call.respondText("Нанчинаем игру!")
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
