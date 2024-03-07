package com.example.features.getMenu

import com.example.entities.DBAdapter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.getMenu() {
    routing {
        get("/menu") {
            call.respond(HttpStatusCode.Forbidden, DBAdapter.getMenu())
        }
    }
}