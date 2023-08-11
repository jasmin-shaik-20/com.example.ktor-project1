package com.example

import com.example.dao.User
import com.example.file.ApiEndPoint
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.example.plugins.*
import io.ktor.client.utils.EmptyContent.contentType

class ApplicationTest {
    @Test
    fun testGetAllUserRoot() = testApplication {
        val response=client.get("/user/"){
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostUserRoot()= testApplication {
        val response=client.post("/user"){
            contentType(ContentType.Application.Json)
            setBody(User(1,"Jasmin"))
        }
        assertEquals("User stored correctly", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }
    @Test
    fun testGetUserRoot()= testApplication {
        val response=client.get("/user/1"){
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.Found,response.status)
    }
    @Test
    fun testDeleteUserRoot()= testApplication {
        val response=client.get("/user/1"){
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }


}
