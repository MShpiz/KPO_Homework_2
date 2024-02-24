package com.example.features.addMealsToMenu

import com.example.entities.Admin
import com.example.entities.AuthenticationManager
import com.example.entities.Visitor
import com.example.features.getMenu.AddMealsToMenuModelRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.addMealsToMenu() {
    routing {
        post("/addMealsToMenu") {
            val result = call.receive<AddMealsToMenuModelRequest>()
            val user = AuthenticationManager.checkToken(result.token)
            if (user == null || user is Visitor){
                call.respond(HttpStatusCode.Forbidden, "authorise as admin first")
            }
            try{
                (user as Admin).dbAdapter.addMeal(result.itemName, result.itemPrice, result.itemAmount)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "smt went wrong")
            }

            call.respond(HttpStatusCode.OK, "added meal menu: " + result.itemName)

        }
    }
}