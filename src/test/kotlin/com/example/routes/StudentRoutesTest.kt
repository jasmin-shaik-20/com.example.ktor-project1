package com.example.routes

import com.example.dao.Student
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class StudentRoutesTest {

    @Test
    fun testGetAllStudents()= testApplication {
        val response=client.get("/student"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostStudent()= testApplication {
        val studentNameMinLength=System.getenv("studentNameMinLength").toIntOrNull()
        val studentNameMaxLength=System.getenv("studentNameMaxLength").toIntOrNull()
        if(studentNameMinLength!=null && studentNameMaxLength!=null) {
            val student = Student(7, "Nasreen")
            if(student.name.length in studentNameMinLength..studentNameMaxLength) {
                val serializedStudent = Json.encodeToString(student)
                val response = client.post("/student") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedStudent)
                }
                assertEquals(HttpStatusCode.Created, response.status)
                val responseStudent = Json.decodeFromString<Student>(response.bodyAsText())
                assertEquals(student, responseStudent)
            }
        }
    }
    @Test
    fun testGetStudent()= testApplication {
        val student= Student(1,"Jasmin")
        val response=client.get("/student/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseStudent= Json.decodeFromString<Student>(response.bodyAsText())
        assertEquals(student,responseStudent)
    }
    @Test
    fun testDeleteStudent()= testApplication {
        val response=client.delete("/student/2"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateStudent()= testApplication {
        val studentNameMinLength=System.getenv("studentNameMinLength").toIntOrNull()
        val studentNameMaxLength=System.getenv("studentNameMaxLength").toIntOrNull()
        if(studentNameMinLength!=null && studentNameMaxLength!=null) {
            val editStudent = Student(1, "Jas")
            if(editStudent.name.length in studentNameMinLength..studentNameMaxLength) {
                val serializedStudent = Json.encodeToString(editStudent)
                val response = client.put("/student/1") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedStudent)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
}