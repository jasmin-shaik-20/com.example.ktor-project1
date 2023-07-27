package com.example.dao



data class Customer(val id: String, var name: String, val email: String)

val customerStorage = mutableListOf<Customer>()