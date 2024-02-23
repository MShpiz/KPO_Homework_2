package com.example.features.orderMeal

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.orderMeal() {
    routing {
        post("/orderMeal") {
            val result = call.receive<OrderMealModelRequest>()

            val authManager = AuthenticationManager
            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Admin){
                call.respond(HttpStatusCode.Forbidden, "authorise as visitor first")
            }
            try {
                (user as Visitor).orderBuilder.addMeal(result.mealName)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message.toString())
            }
            call.respond(HttpStatusCode.OK, "added meal to order: " + result.mealName)
        }
    }
}