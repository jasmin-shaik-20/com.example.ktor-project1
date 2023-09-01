package com.example.services

import com.example.dao.RedisUtils
import com.example.dao.RedisUtils.set
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint.EXPIRE_TIME
import com.example.routes.LoginResult
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

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
            LoginResult.Error("Invalid length of username and password")
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