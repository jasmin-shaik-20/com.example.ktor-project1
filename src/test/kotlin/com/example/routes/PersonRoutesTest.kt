package com.example.routes

import com.example.dao.Person
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonRoutesTest {

    @Test
    fun testPostPerson()= testApplication {
        val person= Person(3,"Sumayia")
        val serializedPerson= Json.encodeToString(person)
        val response=client.post("/person/post details"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedPerson)
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
}