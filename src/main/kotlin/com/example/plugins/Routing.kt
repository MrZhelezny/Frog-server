package com.example.plugins

import com.example.model.User
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    var users = mutableListOf<User>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/api/") {
            call.respondText("Hello Platon!")
        }
        post("/api/") {
            val customer = call.receive<User>()
            users.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
    }
}
