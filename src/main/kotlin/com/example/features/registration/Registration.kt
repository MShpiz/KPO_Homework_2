package com.example.features.registration

import com.example.entities.AuthenticationManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.register() {
    routing {
        post("/register") {
            val result = call.receive<RegistrationModel>()
            println("### ${result.login}, ${result.password}, ${result.role}")
            val authManager = AuthenticationManager
            try{
                authManager.addUser(result.login, result.password, result.role)
            }  catch (e: ArrayStoreException) {
                call.respond(HttpStatusCode.Conflict, e.message.toString())
            } catch (e: Exception) {
                println(e.message)
                call.respond(HttpStatusCode.BadGateway, "something went wrong, try again later")
            }
            call.respond(HttpStatusCode.OK, "successful registration")
        }
    }
}
