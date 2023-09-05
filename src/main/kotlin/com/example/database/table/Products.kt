package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable


object Products: UUIDTable("products"){
    private const val MAX_LENGTH=100
    val userId=reference("userId",Users)
    val name=varchar("name",MAX_LENGTH)
    val price=integer("price")
}









