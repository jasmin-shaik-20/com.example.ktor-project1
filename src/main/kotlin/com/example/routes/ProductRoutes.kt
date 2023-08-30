package com.example.routes

import com.example.endpoints.ApiEndPoint
import com.example.repository.ProductRepository
import com.example.services.ProductServices
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject

fun Application.configureProductRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength = config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength = config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.PRODUCT) {
            val productRepository: ProductRepository by inject()
            val productServices: ProductServices by inject()

            get {
                productServices.handleGetProducts(call,productRepository)
            }

            post {
                productServices.handlePostProduct(call, productRepository, productNameMinLength, productNameMaxLength)
            }

            get("/userId/{id?}") {
                productServices.handleGetProductsByUserId(call, productRepository)
            }

            get("/{id?}") {
                productServices.handleGetProductById(call, productRepository)
            }

            delete("/{id?}") {
                productServices.handleDeleteProduct(call, productRepository)
            }

            put("/{id?}") {
                productServices.handlePutProduct(call, productRepository)
            }
        }
    }
}


