package com.example.database.table

import org.jetbrains.exposed.dao.id.UUIDTable

object Courses : UUIDTable("courses") {
    private const val MAX_LENGTH=100
    val studentId = reference("studentId",Students)
    val name = varchar("name",MAX_LENGTH )
}
