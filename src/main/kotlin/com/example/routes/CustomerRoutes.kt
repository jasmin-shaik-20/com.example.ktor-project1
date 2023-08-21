package com.example.routes

import com.example.dao.Customer
import com.example.dao.customerStorage
import com.example.endpoints.ApiEndPoint
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

fun Application.configureCustomerRoutes() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val customerNameMinLength = config.property("ktor.CustomerValidation.customerNameMinLength").
    getString().toIntOrNull()
    val customerNameMaxLength = config.property("ktor.CustomerValidation.customerNameMaxLength").
    getString().toIntOrNull()

    routing {
        route(ApiEndPoint.CUSTOMER) {
            get {
                handleGetCustomers()
            }

            get("/{id?}") {
                handleGetCustomerById()
            }

            post {
                handlePostCustomer(customerNameMinLength, customerNameMaxLength)
            }

            delete("/{id?}") {
                handleDeleteCustomer()
            }

            put("/{id?}") {
                handlePutCustomer(customerNameMinLength, customerNameMaxLength)
            }

            patch("/{id?}") {
                handlePatchCustomer(customerNameMinLength, customerNameMaxLength)
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetCustomers() {
    if (customerStorage.isNotEmpty()) {
        call.respond(HttpStatusCode.OK,customerStorage)
    } else {
        call.respond("No customers found")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetCustomerById() {
    val id = call.parameters["id"]
    if (id == null) {
        call.respondText("Missing id")
    } else {
        val customer = customerStorage.find { it.id == id }
        if (customer == null) {
            call.respondText("No customer with id $id")
        } else {
            call.respond(customer)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostCustomer(
    customerNameMinLength: Int?,
    customerNameMaxLength: Int?
) {
    val customer = call.receive<Customer>()
    if (customer.name.length in customerNameMinLength!!..customerNameMaxLength!!) {
        customerStorage.add(customer)
        call.respond(HttpStatusCode.OK, "Customer stored correctly")
    } else {
        call.respond("Invalid Length")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleDeleteCustomer() {
    val id = call.parameters["id"]
    if (id == null) {
        call.respondText("Missing id")
    } else {
        if (customerStorage.removeIf { it.id == id }) {
            call.respondText("Customer removed correctly")
        } else {
            call.respondText("No customer to delete with given id $id")
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePutCustomer(
    customerNameMinLength: Int?,
    customerNameMaxLength: Int?
) {
    val id = call.parameters["id"]
    if (id == null) {
        call.respondText("Missing id")
    } else {
        val existingCustomer = customerStorage.find { it.id == id }
        if (existingCustomer == null) {
            call.respondText("No customer with id $id")
        } else {
            val updatedCustomer = call.receive<Customer>()
            if (updatedCustomer.name.length in (customerNameMinLength ?: 0)..(customerNameMaxLength ?: Int.MAX_VALUE)) {
                customerStorage.remove(existingCustomer)
                customerStorage.add(updatedCustomer)
                call.respondText("Customer updated correctly")
            } else {
                call.respond("Invalid Length")
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePatchCustomer(
    customerNameMinLength: Int?,
    customerNameMaxLength: Int?
) {
    val id = call.parameters["id"]
    if (id == null) {
        call.respondText("Missing id")
    } else {
        val name = call.receive<Map<String, String>>()["name"]
        val existingCustomer = customerStorage.find { it.id == id }
        if (existingCustomer != null) {
            if (name != null && name.length in (customerNameMinLength ?: 0)..(customerNameMaxLength ?: Int.MAX_VALUE)) {
                existingCustomer.name = name
                call.respond(existingCustomer)
            } else {
                call.respond("Invalid Length")
            }
        } else {
            call.respondText("Customer not found with the given id")
        }
    }
}


