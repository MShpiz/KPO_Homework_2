package com.example.entities

class Visitor(id: UInt, login: String, passwordHash: Int): User(id, login, passwordHash) {
    val orderBuilder: OrderBuilder = OrderBuilder()
}