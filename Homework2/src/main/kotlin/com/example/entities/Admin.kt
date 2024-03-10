package com.example.entities

class Admin(id: Int, login: String, passwordHash: Int): User(id, login, passwordHash) {
    val dbAdapter: DBAdapter = DBAdapter
}