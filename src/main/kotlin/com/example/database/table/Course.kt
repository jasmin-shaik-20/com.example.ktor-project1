package com.example.database.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Course(val id: Int = 0, val studentId: Int, val name: String)

object Courses : Table("courses") {
    private const val MAX_LENGTH=100
    val id = integer("id").autoIncrement()
    val studentId = integer("studentId") references (Students.id)
    val name = varchar("name",MAX_LENGTH )
    override val primaryKey = PrimaryKey(id)
}
