package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.Login
import com.example.endpoints.ApiEndPoint.TOKEN_EXPIRATION
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginServices {
    suspend fun handleUserLogin(user:Login,
        secret: String,
        issuer: String,
        audience: String,
        loginNameMinLength: Int?,
        loginNameMaxLength: Int?,
        loginPasswordMinLength: Int?,
        loginPasswordMaxLength: Int?
    ): Any {
        if (user.username.length in loginNameMinLength!!..loginNameMaxLength!! &&
            user.password.length in loginPasswordMinLength!!..loginPasswordMaxLength!!
        ) {
            return if (user.username.isNotEmpty() && user.password.isNotEmpty()) {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC256(secret))
                hashMapOf("token" to token)
            } else {
                "Missing Parameters"
            }
        } else {
            return "Invalid length of username and password"
        }
    }

    fun handleAuthenticatedHello(principal: JWTPrincipal): String {
        val username = principal.payload.getClaim("username").asString()
        val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
        return "Hello, $username! Token is expired at $expiresAt ms."
    }
}
