package com.example.features.registration

import com.example.entities.AuthenticationManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/register") {
            val result = call.receive<RegistrationModel>()

            val authManager = AuthenticationManager
            try{
                authManager.addUser(result.login, result.password)
            }  catch (e: ArrayStoreException) {
                call.respond(HttpStatusCode.Conflict, e.message.toString())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong, try again later")
            }
            call.respond(HttpStatusCode.OK, "successful registration")
        }
    }
}
