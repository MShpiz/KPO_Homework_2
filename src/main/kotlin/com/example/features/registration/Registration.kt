package com.example.features.registration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/register") {
            val result = call.receive<RegistrationModel>()

            val dbadapter = DBAdapter()
            try{
                dbadapter.adduser()
            }  catch (e: ArrayStoreException) {
                call.respond(HttpStatusCode.Conflict, "user with this user name already exists")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "something went wrong try again later")
            }
            call.respond(HttpStatusCode.OK, "successful registration")
        }
    }
}
