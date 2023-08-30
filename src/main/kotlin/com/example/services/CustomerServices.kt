package com.example.services

import com.example.dao.Customer
import com.example.dao.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class CustomerServices {
    suspend fun handleGetCustomers(call: ApplicationCall) {
        if (customerStorage.isNotEmpty()) {
            call.respond(HttpStatusCode.OK, customerStorage)
        } else {
            call.respond("No customers found")
        }
    }

    suspend fun handleGetCustomerById(call: ApplicationCall) {
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

    suspend fun handlePostCustomer(call: ApplicationCall,
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

    suspend fun handleDeleteCustomer(call: ApplicationCall) {
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

    suspend fun handlePutCustomer(call: ApplicationCall,
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

    suspend fun handlePatchCustomer(call: ApplicationCall,
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
}