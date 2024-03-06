package com.example.features.increaseMeal

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.increaseMeal() {
    routing {
        post("/increase_meal_amount") {
            val result = call.receive<IncreaseMealModerRequest>()

            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Visitor) {
                call.respond(HttpStatusCode.Forbidden, "authorise as admin first")
            }

            try {
                (user as Admin).dbAdapter.increaseMealAmount(result.meal)
            } catch (e: NullPointerException) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}