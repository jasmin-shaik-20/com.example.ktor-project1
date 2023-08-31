package com.example.routes

import com.example.dao.Customer
import com.example.dao.customerStorage
import com.example.endpoints.ApiEndPoint
import com.example.services.CustomerServices
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.ApplicationCall
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.server.routing.patch
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject

fun Application.configureCustomerRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val customerNameMinLength = config.property("ktor.CustomerValidation.customerNameMinLength").
    getString().toIntOrNull()
    val customerNameMaxLength = config.property("ktor.CustomerValidation.customerNameMaxLength").
    getString().toIntOrNull()

    routing {
        route(ApiEndPoint.CUSTOMER) {
            val customerServices=CustomerServices()
            get {
                customerServices.handleGetCustomers(call)
            }

            get("/{id?}") {
                customerServices.handleGetCustomerById(call)
            }

            post {
                customerServices.handlePostCustomer(call,customerNameMinLength, customerNameMaxLength)
            }

            delete("/{id?}") {
                customerServices.handleDeleteCustomer(call)
            }

            put("/{id?}") {
                customerServices.handlePutCustomer(call,customerNameMinLength, customerNameMaxLength)
            }

            patch("/{id?}") {
                customerServices.handlePatchCustomer(call,customerNameMinLength, customerNameMaxLength)
            }
        }
    }
}





