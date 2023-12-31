package com.example.services
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.LoginConfig.audience
import com.example.config.LoginConfig.issuer
import com.example.config.LoginConfig.secret
import com.example.database.table.Login
import com.example.utils.appConstants.GlobalConstants
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class LoginServicesTest {

    private val loginServices = LoginServices()

    @Test
    fun testHandleUserLoginSuccess() {
        val user = Login("Jasmin", "jasmin@20")
        runBlocking {
            val token = loginServices.handleUserLogin(
                user, secret, issuer, audience, 4, 20, 6, 20
            ) as Map<*, *>
            assertNotNull(token)
            assertTrue(token.containsKey("token"))
        }
    }

    @Test
    fun testHandleUserLoginInvalidLength() {
        val user = Login("Jasmin", "Jasmin@20")
        runBlocking {
            val result = loginServices.handleUserLogin(user, secret, issuer, audience, 2, 5, 6, 20)
            assertEquals("Invalid length of username and password", result)
        }
    }

    @Test
    fun testHandleAuthenticatedHello() {
        runBlocking {
            val username = "Jasmin"
            val token = JWT.create()
                .withClaim("username", username)
                .withExpiresAt(Date(System.currentTimeMillis() + GlobalConstants.TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC256(secret))
            val principal = JWTPrincipal(JWT.decode(token))
            val greeting = loginServices.handleAuthenticatedHello(principal)
            val expiresTime = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            assertEquals("Hello, $username! Token is expired at $expiresTime ms.", greeting)
        }
    }
}
