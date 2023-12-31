package com.example.database.table

import kotlinx.serialization.Serializable

@Serializable
data class Customer(var id: String, var name: String, val email: String)

val customerStorage = mutableListOf<Customer>()
