package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
@Serializable
data class UserProfile(val profileId: Int, val userId: Int, val email: String, val age: Int)

object Profile : Table("UserProfile") {
    val profileId = integer("profileId").autoIncrement()
    val userId = integer("userid").references(Users.id)
    val email = varchar("email", 100)
    val age = integer("age")
    override val primaryKey = PrimaryKey(profileId)
}
