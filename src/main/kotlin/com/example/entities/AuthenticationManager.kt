package com.example.entities

import com.example.storage.ActiveUsers
import io.ktor.server.plugins.*
import java.security.InvalidParameterException
import java.util.*

object AuthenticationManager {
    private val dbAdapter: DBAdapter = DBAdapter()
    private val activeUsers: ActiveUsers = ActiveUsers()
    fun addUser(login: String, hashPassword: String, role: Boolean) {
        try {
            dbAdapter.addUser(login, hashPassword, (if (role) "admin" else "visitor"))
        } catch (e: NullPointerException) {
            throw NullPointerException("Something wrong with database")
        } catch (e: ArrayStoreException) {
            throw ArrayStoreException("User with this login already exists")
        } catch (e: Exception) {
            throw Exception("Something went wrong")
        }
    }
    fun logUserIn(login: String, hashPassword: String): ULong {
        var user: User
        try {
            user = dbAdapter.getUser()
        } catch (e: NullPointerException) {
            throw NotFoundException("no such user")
        } catch (e: Exception) {
            throw Exception("Something went wrong")
        }

        if (activeUsers.activeUsers.values.indexOf(user) == -1) {
            var token: ULong
            do {
                token = (Random().nextInt(1000000000) + 10000000).toULong()
            } while (activeUsers.activeUsers.keys.indexOf(token) == -1)
            activeUsers.activeUsers[token] = user
            return token
        }
        throw InvalidParameterException("user already logged in")
    }

    fun checkToken(token: ULong): User? {
        return activeUsers.activeUsers[token]
    }

    fun logOut(token: ULong) {
        val user = activeUsers.activeUsers[token] ?: throw InvalidParameterException("user not logged in")
        activeUsers.activeUsers.remove(token)
        dbAdapter.addUserActivity(UserActivity(user, "logout"))
    }


}