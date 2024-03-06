package com.example.features.changeMealPrice

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import com.example.features.getUserActivity.ActivityModelRequest
import com.example.features.getUserActivity.ActivityModelResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.changeMealPrice() {
    routing {
        post("/activity") {
            val result = call.receive<ChangeMealPriceModelRequest>()

            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Visitor) {
                call.respond(HttpStatusCode.Forbidden, "authorise as admin first")
            }

            try {
                (user as Admin).dbAdapter.changeMealPrice(result.meal, result.price)
            } catch (e: NullPointerException) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}