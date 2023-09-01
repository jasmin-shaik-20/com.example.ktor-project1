package com.example.routes

import com.example.dao.Customer
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomerRoutesTest {

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
            val customer = Customer("5", "Nasreen", "nasreen@gmail.com")
            if(customer.name.length in customerNameMaxLength..customerNameMinLength) {
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
            val editCustomer = Customer("3", "Divya", "divya@gmail.com")
            if(editCustomer.name.length in customerNameMinLength..customerNameMaxLength) {
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