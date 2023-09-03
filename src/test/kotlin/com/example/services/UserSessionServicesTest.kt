package com.example.services

import com.example.dao.RedisUtils
import com.example.dao.UserSession
import com.example.endpoints.ApiEndPoint
import com.example.routes.LoginResult
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UserSessionServicesTest {

    private val userSessionServices = UserSessionServices()

    @Test
    fun testHandleLogin(){
        runBlocking {
            UserSession(id = "2", username = "Jasmin", password = "Jas@20")
            val result=userSessionServices.handleLogin(2,10,3,10)
            assertTrue(result is LoginResult.Success)
            assertEquals("Jasmin", (result as LoginResult.Success).userSession.username)
        }
    }

    @Test
    fun testHandleUserSession() {
        runBlocking {
            val userSession = UserSession(id = "2", username = "Jasmin", password = "Jas@20")
            val sessionId = "session123"
            RedisUtils.set(sessionId, userSession.toJson(), ApiEndPoint.EXPIRE_TIME)
            val result=userSessionServices.handleUserSession(sessionId)
            assertEquals("Username is ${userSession.username}",result)
        }
    }

    @Test
    fun testHandleLogout() {
        runBlocking {
            val userSessionServices = UserSessionServices()
            val sessionId = "session123"
            RedisUtils.set(sessionId, "SomeUserSessionJson", ApiEndPoint.EXPIRE_TIME)
            val result = userSessionServices.handleLogout(sessionId)
            assertEquals("Logout successful!", result)
            assertNull(RedisUtils.get(sessionId))
        }
    }
}