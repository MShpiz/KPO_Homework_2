package com.example.entities

class Visitor(id: Int, login: String, passwordHash: Int): User(id, login, passwordHash) {
    val orderBuilder: OrderBuilder = OrderBuilder()
}