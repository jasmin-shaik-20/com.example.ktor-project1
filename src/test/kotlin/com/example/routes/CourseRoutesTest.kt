package com.example.routes

import com.example.dao.Course
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class CourseRoutesTest {

    @Test
    fun testGetAllCourses()= testApplication {
        val response=client.get("/course"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostCourse()= testApplication {
        val courseNameMinLength=System.getenv("courseNameMinLength").toIntOrNull()
        val courseNameMaxLength=System.getenv("courseNameMaxLength").toIntOrNull()
        if(courseNameMinLength!=null && courseNameMaxLength!=null) {
            val course = Course(16, 5, "HTML")
            if(course.name.length in courseNameMinLength..courseNameMaxLength) {
                val serializedCourse = Json.encodeToString(course)
                val response = client.post("/course") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedCourse)
                }
                assertEquals(HttpStatusCode.Created, response.status)
                val responseCourse = Json.decodeFromString<Course>(response.bodyAsText())
                assertEquals(course, responseCourse)
            }
        }
    }
    @Test
    fun testGetCourse()= testApplication {
        val course= Course(1,1,"Maths")
        val response=client.get("/course/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseCourse= Json.decodeFromString<Course>(response.bodyAsText())
        assertEquals(course,responseCourse)
    }
    @Test
    fun testDeleteCourse()= testApplication {
        val response=client.delete("/course/3"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateCourse()= testApplication {
        val courseNameMinLength=System.getenv("courseNameMinLength").toIntOrNull()
        val courseNameMaxLength=System.getenv("courseNameMaxLength").toIntOrNull()
        if(courseNameMinLength!=null && courseNameMaxLength!=null) {
            val editCourse = Course(7, 2, "C++")
            if (editCourse.name.length in courseNameMinLength..courseNameMaxLength) {
                val serializedCourse = Json.encodeToString(editCourse)
                val response = client.put("/course/7") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedCourse)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
}