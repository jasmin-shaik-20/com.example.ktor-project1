package com.example.routes

import com.example.dao.Product
import com.example.endpoints.ApiEndPoint
import com.example.repository.ProductRepository
import com.example.services.ProductServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.configureProductRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength = config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength = config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.PRODUCT) {

            val productServices: ProductServices by inject()

            get {
                val products = productServices.handleGetProducts()
                call.respond(products)
                call.application.environment.log.info("Returned a list of products")
            }

            post {
                val productDetails = call.receive<Product>()
                val product = productServices.handlePostProduct(
                    productDetails,
                    productNameMinLength,
                    productNameMaxLength
                )
                call.respond(HttpStatusCode.Created, product)
                call.application.environment.log.info("Created a new product: $product")
            }

            get("/userId/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond("Missing id")
                val products = productServices.handleGetProductsByUserId(id)
                call.respond(products)
                call.application.environment.log.info("Returned products for user with ID: $id")
            }

            get("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond("Missing id")
                val product = productServices.handleGetProductById(id)
                call.respond(product)
                call.application.environment.log.info("Returned product with ID: $id")
            }

            delete("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond("Missing id")
                productServices.handleDeleteProduct(id)
                call.respond(HttpStatusCode.OK, "Product deleted successfully")
                call.application.environment.log.info("Deleted product with ID: $id")
            }

            put("/{id?}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond("Missing id")
                val productDetails = call.receive<Product>()
                productServices.handleUpdateProduct(id,productDetails)
                call.respond(HttpStatusCode.OK, "Product updated successfully")
                call.application.environment.log.info("Updated product with ID: $id")
            }
        }
    }
}
