package com.example.routes

import com.example.dao.Product
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.ProductInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Application.configureProductRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength = config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength = config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.PRODUCT) {
            val productInterfaceImpl: ProductInterfaceImpl by inject()

            get {
                handleGetProducts(productInterfaceImpl)
            }

            post {
                handlePostProduct(call, productInterfaceImpl, productNameMinLength, productNameMaxLength)
            }

            get("/userId/{id?}") {
                handleGetProductsByUserId(call, productInterfaceImpl)
            }

            get("/{id?}") {
                handleGetProductById(call, productInterfaceImpl)
            }

            delete("/{id?}") {
                handleDeleteProduct(call, productInterfaceImpl)
            }

            put("/{id?}") {
                handlePutProduct(call, productInterfaceImpl)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetProducts(productInterfaceImpl: ProductInterfaceImpl) {
    val getProducts = productInterfaceImpl.getAllProducts()
    if (getProducts.isEmpty()) {
        throw ProductNotFoundException()
    } else {
        application.environment.log.info("All product details")
        call.respond(getProducts)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostProduct(
    call: ApplicationCall,
    productInterfaceImpl: ProductInterfaceImpl,
    productNameMinLength: Int?,
    productNameMaxLength: Int?
) {
    val insert = call.receive<Product>()
    if (insert.name.length in productNameMinLength!!..productNameMaxLength!!) {
        val postProduct = productInterfaceImpl.insertProduct(insert.productId, insert.userId, insert.name, insert.price)
        if (postProduct != null) {
            application.environment.log.info("Product is created")
            call.respond(HttpStatusCode.Created, postProduct)
        } else {
            call.respond(HttpStatusCode.InternalServerError)
        }
    } else {
        call.respond("Invalid Length")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetProductsByUserId(
    call: ApplicationCall,
    productInterfaceImpl: ProductInterfaceImpl
) {
    val id = call.parameters["id"] ?: throw InvalidIDException()
    val getProduct = productInterfaceImpl.getProductsById(id.toInt())
    if (getProduct.isEmpty()) {
        application.environment.log.error("No product found with given id")
        throw ProductNotFoundException()
    } else {
        application.environment.log.info("Product list with given id is found")
        call.respond(getProduct)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetProductById(
    call: ApplicationCall,
    productInterfaceImpl: ProductInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    val fetid = productInterfaceImpl.getProduct(id!!.toInt())
    if (fetid != null) {
        application.environment.log.info("Product is found")
        call.respond(fetid)
    } else {
        throw ProductNotFoundException()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteProduct(
    call: ApplicationCall,
    productInterfaceImpl: ProductInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val delProduct = productInterfaceImpl.deleteProduct(id)
        if (delProduct) {
            application.environment.log.info("Product is deleted")
            call.respond(HttpStatusCode.OK)
        } else {
            application.environment.log.error("No product found with given id")
            throw ProductNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutProduct(
    call: ApplicationCall,
    productInterfaceImpl: ProductInterfaceImpl
) {
    val id = call.parameters["id"]?.toIntOrNull()
    if (id != null) {
        val product = call.receive<Product>()
        val editProduct = productInterfaceImpl.editProduct(product.productId, product.name, product.price)
        if (editProduct) {
            application.environment.log.info("Product is updated")
            call.respond(HttpStatusCode.OK)
        } else {
            throw ProductNotFoundException()
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}
