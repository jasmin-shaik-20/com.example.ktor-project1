package com.example.services

import com.example.dao.Product
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import com.example.repository.ProductInterfaceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class ProductServices {
    suspend fun handleGetProducts
                (call:ApplicationCall,productInterfaceImpl: ProductInterfaceImpl) {
        val getProducts = productInterfaceImpl.getAllProducts()
        if (getProducts.isEmpty()) {
            throw ProductNotFoundException()
        } else {
            call.application.environment.log.info("All product details")
            call.respond(getProducts)
        }
    }

    suspend fun handlePostProduct(
        call: ApplicationCall,
        productInterfaceImpl: ProductInterfaceImpl,
        productNameMinLength: Int?,
        productNameMaxLength: Int?
    ) {
        val insert = call.receive<Product>()
        if (insert.name.length in productNameMinLength!!..productNameMaxLength!!) {
            val postProduct = productInterfaceImpl.insertProduct(insert.productId, insert.userId, insert.name, insert.price)
            if (postProduct != null) {
                call.application.environment.log.info("Product is created")
                call.respond(HttpStatusCode.Created, postProduct)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        } else {
            call.respond("Invalid Length")
        }
    }

    suspend fun handleGetProductsByUserId(
        call: ApplicationCall,
        productInterfaceImpl: ProductInterfaceImpl
    ) {
        val id = call.parameters["id"] ?: throw InvalidIDException()
        val getProduct = productInterfaceImpl.getProductsById(id.toInt())
        if (getProduct.isEmpty()) {
            call.application.environment.log.error("No product found with given id")
            throw ProductNotFoundException()
        } else {
            call.application.environment.log.info("Product list with given id is found")
            call.respond(getProduct)
        }
    }

    suspend fun handleGetProductById(
        call: ApplicationCall,
        productInterfaceImpl: ProductInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        val fetid = productInterfaceImpl.getProduct(id!!.toInt())
        if (fetid != null) {
            call.application.environment.log.info("Product is found")
            call.respond(fetid)
        } else {
            throw ProductNotFoundException()
        }
    }

    suspend fun handleDeleteProduct(
        call: ApplicationCall,
        productInterfaceImpl: ProductInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val delProduct = productInterfaceImpl.deleteProduct(id)
            if (delProduct) {
                call.application.environment.log.info("Product is deleted")
                call.respond(HttpStatusCode.OK)
            } else {
                call.application.environment.log.error("No product found with given id")
                throw ProductNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    suspend fun handlePutProduct(
        call: ApplicationCall,
        productInterfaceImpl: ProductInterfaceImpl
    ) {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id != null) {
            val product = call.receive<Product>()
            val editProduct = productInterfaceImpl.editProduct(product.productId, product.name, product.price)
            if (editProduct) {
                call.application.environment.log.info("Product is updated")
                call.respond(HttpStatusCode.OK)
            } else {
                throw ProductNotFoundException()
            }
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}