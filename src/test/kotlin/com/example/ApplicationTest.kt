package com.example

import com.example.dao.Product
import com.example.dao.User
import com.example.dao.UserProfile
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ApplicationTest {

    //user
    @Test
    fun testGetAllUserRoot() = testApplication {
        val response=client.get("/user/"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
//    @Test
//    fun testPostUserRoot()= testApplication {
//        val user = User(22, "stu")
//        val serializedUser = Json.encodeToString(user)
//        val response = client.post("/user") {
//            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
//            setBody(serializedUser)
//        }
//        assertEquals(HttpStatusCode.Created, response.status)
//        val responseUser = Json.decodeFromString<User>(response.bodyAsText())
//        assertEquals(user, responseUser)
//    }
    @Test
    fun testGetUserRoot()= testApplication {
        val user=User(3,"sumayia")
        val response=client.get("/user/3"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseUser = Json.decodeFromString<User>(response.bodyAsText())
        assertEquals(user, responseUser)
    }
    @Test
    fun testDeleteUserRoot()= testApplication {
        val response=client.get("/user/4"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateUserRoot()= testApplication {
        val user=User(2,"Divya")
        val editUser=User(21,"div")
        val serializedUser = Json.encodeToString(editUser)
        val response=client.put("/user/21"){
            headers[HttpHeaders.ContentType]=ContentType.Application.Json.toString()
            setBody(serializedUser)
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }

    //UserProfile
    @Test
    fun testGetAllProfile()= testApplication {
        val response=client.get("/userProfile"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }

    @Test
    fun testPostProfile()= testApplication {
        val profile= UserProfile(3,3, "sumyaia@gmail.com",21)
        val serializedProfile=Json.encodeToString(profile)
        val response=client.post("/userProfile"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedProfile)
        }
        assertEquals(HttpStatusCode.Created,response.status)
        val responseUserProfile = Json.decodeFromString<UserProfile>(response.bodyAsText())
        assertEquals(profile,responseUserProfile)
    }

    @Test
    fun testGetProfile()= testApplication {
        val profile=UserProfile(2,2,"div@gmail.com",21)
        val response=client.get("/userProfile/2"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseUserProfile = Json.decodeFromString<UserProfile>(response.bodyAsText())
        assertEquals(profile, responseUserProfile)
    }
    @Test
    fun testDeleteProfile()= testApplication {
        val response=client.delete("/userProfile/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateProfile()= testApplication {
        val profile=UserProfile(2,2,"div@gmail.com",21)
        val editProfile=UserProfile(4,4,"divya@gmail.com",22)
        val serializedUserProfile = Json.encodeToString(editProfile)
        val response=client.put("/userProfile/4"){
            headers[HttpHeaders.ContentType]=ContentType.Application.Json.toString()
            setBody(serializedUserProfile)
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }

    //Product
    @Test
    fun testGetAllProduct()= testApplication {
        val response=client.get("/product"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostProduct()= testApplication {
        val product=Product(4,2,"cookie",80)
        val serializedProduct=Json.encodeToString(product)
        val response=client.post("/product"){
            headers[HttpHeaders.ContentType]=ContentType.Application.Json.toString()
            setBody(serializedProduct)
        }
        assertEquals(HttpStatusCode.Created,response.status)
        val responseProduct=Json.decodeFromString<Product>(response.bodyAsText())
        assertEquals(product,responseProduct)
    }


}
