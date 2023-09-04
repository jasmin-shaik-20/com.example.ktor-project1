package com.example.routes

import com.example.database.table.Product
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductRoutesTest {
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
            val product = Product(10, 2, "ice", 90)
            if(product.name.length in productNameMinLength..productNameMaxLength) {
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
        val product= Product(5,1,"chocolate",80)
        val response=client.get("/product/5"){
            headers[HttpHeaders.ContentType]= ContentType.Application.Json.toString()
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
            val editProduct = Product(5, 2, "guava", 40)
            if(editProduct.name.length in productNameMinLength..productNameMaxLength) {
                val serializedProduct = Json.encodeToString(editProduct)
                val response = client.put("/product/7") {
                    headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
                    setBody(serializedProduct)
                }
                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
    }
}