package com.example.features.getUserActivity

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.UserActivity
import com.example.entities.Visitor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.getUserActivity() {
    routing {
        post("/activity") {
            val result = call.receive<ActivityModelRequest>()

            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Visitor) {
                call.respond(HttpStatusCode.Forbidden, "authorise as admin first")
            }
            var response: List<UserActivity> = listOf()

            try {
                response = (user as Admin).dbAdapter.getUserActivity()
            } catch (e: NullPointerException) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway)
            }
            call.respond(HttpStatusCode.OK, ActivityModelResponse(response))
        }
    }
}