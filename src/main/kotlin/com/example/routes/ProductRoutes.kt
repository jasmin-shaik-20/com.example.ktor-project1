package com.example.routes


import com.example.config.ProductConfig.productNameMaxLength
import com.example.config.ProductConfig.productNameMinLength
import com.example.entities.ProductEntity
import com.example.services.ProductServices
import com.example.utils.appConstants.ApiEndPoints
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureProductRoutes() {

    routing {
        route(ApiEndPoints.PRODUCT) {

            val productServices: ProductServices by inject()

            get {
                val products = productServices.handleGetProducts()
                call.respond(products)
                call.application.environment.log.info("Returned a list of products")
            }

            post {
                val productDetails = call.receive<ProductEntity>()
                val product = productServices.handlePostProduct(
                    productDetails,
                    productNameMinLength,
                    productNameMaxLength
                )
                call.respond(HttpStatusCode.Created, product)
                call.application.environment.log.info("Created a new product: $product")
            }

            get("/userId/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull()?: return@get call.respond("Missing id")
                val products = productServices.handleGetProductsByUserId(id)
                call.respond(products)
                call.application.environment.log.info("Returned products for user with ID: $id")
            }

            get("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull() ?: return@get call.respond("Missing id")
                val product = productServices.handleGetProductById(id)
                call.respond(product)
                call.application.environment.log.info("Returned product with ID: $id")
            }

            delete("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull() ?: return@delete call.respond("Missing id")
                productServices.handleDeleteProduct(id)
                call.respond(HttpStatusCode.OK, "Product deleted successfully")
                call.application.environment.log.info("Deleted product with ID: $id")
            }

            put("/{id?}") {
                val id = runCatching { UUID.fromString(call.parameters["id"] ?: "") }
                    .getOrNull() ?: return@put call.respond("Missing id")
                val productDetails = call.receive<ProductEntity>()
                productServices.handleUpdateProduct(id,productDetails)
                call.respond(HttpStatusCode.OK, "Product updated successfully")
                call.application.environment.log.info("Updated product with ID: $id")
            }
        }
    }
}
