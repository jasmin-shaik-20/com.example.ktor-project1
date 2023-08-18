package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserProfile(val profileId: Int=0, val userId: Int, val email: String, val age: Int)

object Profile : Table("UserProfile") {
    private const val MAX_LENGTH=100
    val profileId = integer("profileId").autoIncrement()
    val userId = integer("userid").references(Users.id).uniqueIndex()
    val email = varchar("email", MAX_LENGTH)
    val age = integer("age")
    override val primaryKey = PrimaryKey(profileId)
}

