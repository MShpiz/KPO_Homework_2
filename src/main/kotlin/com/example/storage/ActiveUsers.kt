package com.example.storage

import com.example.entities.User

class ActiveUsers() {
    val activeUsers:MutableMap<ULong, User> = mutableMapOf()
}