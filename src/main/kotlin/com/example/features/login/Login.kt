package com.example.features.login

import com.example.entities.User
import com.example.features.registration.RegistrationModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.login() {
    routing {
        post("/register") {
            val result = call.receive<RegistrationModel>()

            val dbadapter = DBAdapter()
            val user: User
            try{
                user = dbadapter.getUser()
            }  catch (e: ArrayStoreException) {
                call.respond(HttpStatusCode.Conflict, "no such user")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong try again later")
            }
            call.respond(HttpStatusCode.OK, "successful registration")
        }
    }
}
