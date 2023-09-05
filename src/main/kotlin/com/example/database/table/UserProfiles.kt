package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object UserProfiles : UUIDTable("userProfiles") {
    private const val MAX_LENGTH = 100
    val userId = reference("userId", Users)
    val email = varchar("email", MAX_LENGTH)
    val age = integer("age")
}


