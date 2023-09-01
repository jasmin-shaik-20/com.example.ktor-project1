package com.example.routes

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class StudentCoursesRoutesTest {

    @Test
    fun testGetCourseById()= testApplication {
        val response=client.get("/student-course/student/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testGetStudentById()= testApplication {
        val response=client.get("/student-course/course/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
}