package com.example.routes

import com.example.dao.Product
import com.example.endpoints.ApiEndPoint
import com.example.repository.ProductInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import com.example.services.ProductServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject

fun Application.configureProductRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength = config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength = config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()

    routing {
        route(ApiEndPoint.PRODUCT) {
            val productInterfaceImpl: ProductInterfaceImpl by inject()
            val productServices: ProductServices by inject()

            get {
                productServices.handleGetProducts(call,productInterfaceImpl)
            }

            post {
                productServices.handlePostProduct(call, productInterfaceImpl, productNameMinLength, productNameMaxLength)
            }

            get("/userId/{id?}") {
                productServices.handleGetProductsByUserId(call, productInterfaceImpl)
            }

            get("/{id?}") {
                productServices.handleGetProductById(call, productInterfaceImpl)
            }

            delete("/{id?}") {
                productServices.handleDeleteProduct(call, productInterfaceImpl)
            }

            put("/{id?}") {
                productServices.handlePutProduct(call, productInterfaceImpl)
            }
        }
    }
}


