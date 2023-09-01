package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.Login
import com.example.endpoints.ApiEndPoint
import com.typesafe.config.ConfigFactory
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginRoutesTest {
    @Test
    fun testPostLogin() = testApplication {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val secret = config.property("ktor.jwt.secret").getString()
        val issuer = config.property("ktor.jwt.issuer").getString()
        val audience = config.property("ktor.jwt.audience").getString()
        val loginNameMinLength = System.getenv("loginNameMinLength")?.toIntOrNull()
        val loginNameMaxLength = System.getenv("loginNameMaxLength")?.toIntOrNull()
        val loginPasswordMinLength = System.getenv("loginPasswordMinLength")?.toIntOrNull()
        val loginPasswordMaxLength = System.getenv("loginPasswordMaxLength")?.toIntOrNull()

        if (loginNameMinLength != null && loginNameMaxLength != null &&
            loginPasswordMinLength != null && loginPasswordMaxLength != null) {
            val user = Login("Divya", "div@123")
            if (user.username.length in loginNameMinLength..loginNameMaxLength &&
                user.password.length in loginPasswordMinLength..loginPasswordMaxLength) {
                val serializedUser= Json.encodeToString(user)
                JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + ApiEndPoint.TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC256(secret))
                val response = client.post("/userLogin/login") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUser)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    @Test
    fun testGetUserLogin()= testApplication {
        val response=client.get("/userLogin/hello")
        assertEquals(HttpStatusCode.Unauthorized,response.status)
    }
}