package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable


object Users : UUIDTable("users") {
    private const val MAX_LENGTH = 100
    var name = varchar("name", MAX_LENGTH)
}


