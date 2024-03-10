package com.example.features.cancelOrder

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.cancelOrder() {
    routing {
        post("/cancel_order") {
            val result = call.receive<CancelOrderModelRequest>()

            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Admin){
                call.respond(HttpStatusCode.Forbidden, "authorise as visitor first")
            }

            try {
                (user as Visitor).orderBuilder.cancelOrder()
            } catch (e: NoSuchMethodException){
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "smth went wrong")
            }

            call.respond(HttpStatusCode.OK, "order canceled")
        }
    }
}