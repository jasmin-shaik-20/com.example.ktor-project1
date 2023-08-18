package com.example.dao

import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: String, var name: String, val email: String)

val customerStorage = mutableListOf<Customer>()
