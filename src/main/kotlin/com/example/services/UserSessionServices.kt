package com.example.services

import com.example.database.table.UserSession
import com.example.exceptions.UserSessionsInvalidLengthException
import com.example.redis.RedisUtils
import com.example.routes.LoginResult

class UserSessionServices {
    fun handleLogin(
        sessionNameMinLength: Int?,
        sessionNameMaxLength: Int?,
        sessionPasswordMinLength: Int?,
        sessionPasswordMaxLength: Int?
    ): LoginResult {
        val userSession = UserSession(id = "2", username = "Jasmin", password = "Jas@20")
        return if (userSession.username.length in sessionNameMinLength!!..sessionNameMaxLength!! &&
            userSession.password.length in sessionPasswordMinLength!!..sessionPasswordMaxLength!!
        ) {
            LoginResult.Success(userSession)
        } else {
            throw UserSessionsInvalidLengthException()
        }
    }
    fun handleUserSession(sessionId: String): String {
        val userSessionJson = RedisUtils.get(sessionId)
        return if (userSessionJson != null) {
            val userSession = UserSession.fromJson(userSessionJson)
            "Username is ${userSession.username}"
        } else {
            "Session doesn't exist or has expired."
        }
    }

    fun handleLogout(sessionId: String): String {
        RedisUtils.delete(sessionId)
        return "Logout successful!"
    }


}