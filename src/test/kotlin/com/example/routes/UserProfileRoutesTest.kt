package com.example.routes

import com.example.database.table.UserProfile
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileRoutesTest {

    @Test
    fun testGetAllProfile()= testApplication {
        val response=client.get("/userProfile"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostProfile()= testApplication {
        val emailMinLength=System.getenv("emailMinLength").toIntOrNull()
        val emailMaxLength=System.getenv("emailMaxLength").toIntOrNull()
        if(emailMinLength!=null&& emailMaxLength!=null) {
            val profile= UserProfile(8,8, "yas@gmail.com",21)
            if(profile.email.length in emailMinLength..emailMaxLength) {
                val serializedProfile = Json.encodeToString(profile)
                val response = client.post("/userProfile") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedProfile)
                }
                assertEquals(HttpStatusCode.Created, response.status)
                val responseUserProfile = Json.decodeFromString<UserProfile>(response.bodyAsText())
                assertEquals(profile, responseUserProfile)
            }
        }
    }

    @Test
    fun testGetProfile()= testApplication {
        val profile= UserProfile(2,2,"divya@gmail.com",21)
        val response=client.get("/userProfile/2"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseUserProfile = Json.decodeFromString<UserProfile>(response.bodyAsText())
        assertEquals(profile, responseUserProfile)
    }
    @Test
    fun testDeleteProfile()= testApplication {
        val response=client.delete("/userProfile/5"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateProfile()= testApplication {
        val emailMinLength=System.getenv("emailMinLength").toIntOrNull()
        val emailMaxLength=System.getenv("emailMaxLength").toIntOrNull()
        if(emailMinLength!=null && emailMaxLength!=null) {
            val editProfile = UserProfile(2, 2, "jasmin@gmail.com", 22)
            if(editProfile.email.length in emailMinLength..emailMaxLength) {
                val serializedUserProfile = Json.encodeToString(editProfile)
                val response = client.put("/userProfile/5") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUserProfile)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
}