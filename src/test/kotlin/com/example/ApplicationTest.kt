package com.example

import com.example.dao.*
import com.typesafe.config.ConfigFactory
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.dsl.module

class ApplicationTest {

    //user
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
            val user = User(10, "Rishitha")
            if (user.name.length in nameMinLength..nameMaxLength) {
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
        val user=User(3,"Sumayia")
        val response = client.get("/user/3") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
            assertEquals(HttpStatusCode.OK, response.status)
            val responseUser = Json.decodeFromString<User>(response.bodyAsText())
            assertEquals(user, responseUser)
    }
    @Test
    fun testDeleteUserRoot()= testApplication {
        val response=client.get("/user/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateUserRoot() = testApplication {
        val nameMinLength = System.getenv("nameMinLength")?.toIntOrNull()
        val nameMaxLength = System.getenv("nameMaxLength")?.toIntOrNull()
        if (nameMinLength != null && nameMaxLength != null) {
            val user = User(2, "Divya")
            val editUser = User(4, "nani")
            if (editUser.name.length in nameMinLength..nameMaxLength) {
                val serializedUser = Json.encodeToString(editUser)
                val response = client.put("/user/21") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUser)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    //profile
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
            val profile= UserProfile(5,5, "def@gmail.com",21)
            if(profile.email.length in emailMinLength!!..emailMaxLength!!) {
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
        val profile=UserProfile(2,2,"divya@gmail.com",21)
        val response=client.get("/userProfile/2"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseUserProfile = Json.decodeFromString<UserProfile>(response.bodyAsText())
        assertEquals(profile, responseUserProfile)
    }
    @Test
    fun testDeleteProfile()= testApplication {
        val response=client.delete("/userProfile/3"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateProfile()= testApplication {
        val emailMinLength=System.getenv("emailMinLength").toIntOrNull()
        val emailMaxLength=System.getenv("emailMaxLength").toIntOrNull()
        if(emailMinLength!=null && emailMaxLength!=null) {
            val editProfile = UserProfile(4, 4, "div@gmail.com", 22)
            if(editProfile.email.length in emailMinLength!!..emailMaxLength!!) {
                val serializedUserProfile = Json.encodeToString(editProfile)
                val response = client.put("/userProfile/4") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedUserProfile)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
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
        val productNameMinLength=System.getenv("productNameMinLength").toIntOrNull()
        val productNameMaxLength=System.getenv("productNameMaxLength").toIntOrNull()
        if(productNameMinLength!=null && productNameMaxLength!=null) {
            val product = Product(5, 1, "chocolate", 80)
            if(product.name.length in productNameMinLength!!..productNameMaxLength!!) {
                val serializedProduct = Json.encodeToString(product)
                val response = client.post("/product") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedProduct)
                }
                assertEquals(HttpStatusCode.Created, response.status)
                val responseProduct = Json.decodeFromString<Product>(response.bodyAsText())
                assertEquals(product, responseProduct)
            }
        }
    }
    @Test
    fun testGetProduct()= testApplication {
        val product=Product(5,1,"chocolate",80)
        val response=client.get("/product/5"){
            headers[HttpHeaders.ContentType]=ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseUserProduct = Json.decodeFromString<Product>(response.bodyAsText())
        assertEquals(product, responseUserProduct)
    }
    @Test
    fun testDeleteProduct()= testApplication {
        val response=client.delete("/product/3"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateProduct()= testApplication {
        val productNameMinLength=System.getenv("productNameMinLength").toIntOrNull()
        val productNameMaxLength=System.getenv("productNameMaxLength").toIntOrNull()
        if(productNameMinLength!=null && productNameMaxLength!=null) {
            val editProduct = Product(8, 2, "cake", 40)
            if(editProduct.name.length in productNameMinLength!!..productNameMaxLength!!) {
                val serializedProduct = Json.encodeToString(editProduct)
                val response = client.put("/product/8") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedProduct)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    //student
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
            val student = Student(5, "abc")
            if(student.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
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
        val student=Student(1,"Jasmin")
        val response=client.get("/student/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseStudent=Json.decodeFromString<Student>(response.bodyAsText())
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
            val editStudent = Student(7, "Jas")
            if(editStudent.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
                val serializedStudent = Json.encodeToString(editStudent)
                val response = client.put("/student/7") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedStudent)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    //course
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
            val course = Course(2, 1, "Science")
            if(course.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
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
        val course=Course(1,1,"Maths")
        val response=client.get("/course/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
        val responseCourse=Json.decodeFromString<Course>(response.bodyAsText())
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
            val editCourse = Course(4, 5, "Social")
            if (editCourse.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
                val serializedCourse = Json.encodeToString(editCourse)
                val response = client.put("/course/4") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedCourse)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }

    //studentCourses
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

    //customer
    @Test
    fun testGetAllCustomers()= testApplication {
        val response=client.get("/customer"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testPostCustomer() = testApplication {
        val customerNameMinLength=System.getenv("customerNameMinLength").toIntOrNull()
        val customerNameMaxLength=System.getenv("customerNameMaxLength").toIntOrNull()
        if(customerNameMinLength!=null && customerNameMaxLength!=null) {
            val customer = Customer("2", "Divya", "div@gmail.com")
            if(customer.name.length in customerNameMaxLength!!..customerNameMinLength!!) {
                val serializedCustomer = Json.encodeToString(customer)
                val response = client.post("/customer") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedCustomer)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
    @Test
    fun testGetCustomer()= testApplication {
        val customer=Customer("1","Jasmin","jas@123")
        val response=client.get("/customer/1"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testDeleteCustomer()= testApplication {
        val response=client.delete("/customer/2"){
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK,response.status)
    }
    @Test
    fun testUpdateCustomer()= testApplication {
        val customerNameMinLength=System.getenv("customerNameMinLength").toIntOrNull()
        val customerNameMaxLength=System.getenv("customerNameMaxLength").toIntOrNull()
        if(customerNameMinLength!=null && customerNameMaxLength!=null) {
            val editCustomer = Customer("4", "Jas", "jasmin@gmail.com")
            if(editCustomer.name.length in customerNameMinLength!!..customerNameMaxLength!!) {
                val serializedCustomer = Json.encodeToString(editCustomer)
                val response = client.put("/customer/4") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedCustomer)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }




}
