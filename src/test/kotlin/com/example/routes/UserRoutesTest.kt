package com.example.routes

import com.example.dao.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRoutesTest {
    @Test
    fun testGetAllUserRoot() = testApplication {
        val response=client.get("/user"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostUser() = testApplication {
        val nameMinLength = System.getenv("nameMinLength")?.toIntOrNull()
        val nameMaxLength = System.getenv("nameMaxLength")?.toIntOrNull()
        if (nameMinLength != null && nameMaxLength != null) {
            val user = User(15, "Nasreen")
            if (user.name.length in (nameMinLength..nameMaxLength)) {
                val serializedUser = Json.encodeToString(user)
                val response = client.post("/user") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUser)
                }
                assertEquals(HttpStatusCode.Created, response.status)
                val responseUser = Json.decodeFromString<User>(response.bodyAsText())
                assertEquals(user, responseUser)
            }
        }
    }
    @Test
    fun testGetUserRoot()= testApplication {
        val user= User(3,"Sumayia")
        val response = client.get("/user/3") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val responseUser = Json.decodeFromString<User>(response.bodyAsText())
        assertEquals(user, responseUser)
    }
    @Test
    fun testDeleteUserRoot()= testApplication {
        val response=client.get("/user/3"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateUserRoot() = testApplication {
        val nameMinLength = System.getenv("nameMinLength")?.toIntOrNull()
        val nameMaxLength = System.getenv("nameMaxLength")?.toIntOrNull()
        if (nameMinLength != null && nameMaxLength != null) {
            val editUser = User(2, "DivyaDeepika")
            if (editUser.name.length in nameMinLength..nameMaxLength) {
                val serializedUser = Json.encodeToString(editUser)
                val response = client.put("/user/2") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUser)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
}