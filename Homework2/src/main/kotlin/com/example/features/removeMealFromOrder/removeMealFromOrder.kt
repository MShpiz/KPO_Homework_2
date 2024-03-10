package com.example.features.removeMealFromOrder

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import com.example.features.placeOrder.PlaceOrderModelRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.removeMealFromOrder() {
    routing {
        post("/remove_meal_from_order") {
            val result = call.receive<RemoveMealModelRequest>()

            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Admin) {
                call.respond(HttpStatusCode.Forbidden, "authorise as visitor first")
            }
            try {
                (user as Visitor).orderBuilder.removeMeal(result.mealId)
            } catch (e: NoSuchMethodException) {
                call.respond(HttpStatusCode.MethodNotAllowed, e.message.toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "can not remove this meal from the order")
            }
            call.respond(HttpStatusCode.OK, "meal removed")
        }
    }
}