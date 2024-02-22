package com.example.entities

class Admin(id: UInt, login: String, passwordHash: Int): User(id, login, passwordHash) {
    val dbAdapter: DBAdapter = DBAdapter()
}