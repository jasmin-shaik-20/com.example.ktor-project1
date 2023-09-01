package com.example.routes

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UserSessionRoutesTest {
    @Test
    fun testGetLogin()= testApplication {
        val response=client.get("/session/login")
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testGetSession()= testApplication {
        val response=client.get("/session/userSession")
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testGetLogout()= testApplication {
        val response=client.get("/session/logout")
        assertEquals(HttpStatusCode.OK,response.status)
    }
}