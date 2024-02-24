package com.example.features.placeOrder

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import com.example.features.orderMeal.OrderMealModelRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.placeOrder() {
    routing {
        post("/place_order") {
            val result = call.receive<PlaceOrderModelRequest>()

            val authManager = AuthenticationManager
            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Admin) {
                call.respond(HttpStatusCode.Forbidden, "authorise as visitor first")
            }
            try {
                (user as Visitor).orderBuilder.cookOrder()
            } catch (e: NoSuchMethodException) {
                call.respond(HttpStatusCode.MethodNotAllowed, e.message.toString())
            }
            call.respond(HttpStatusCode.OK, "order cooking")
        }
    }
}