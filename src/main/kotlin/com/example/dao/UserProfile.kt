package com.example.dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class UserProfile(val profileid: Int, val userid: Int, val email: String, val age: Int)

object Profile : Table("UserProfile_table") {
    val profileid = integer("profileid").autoIncrement()
    val userid = integer("userid").references(Users.id)
    val email = varchar("email", 100)
    val age = integer("age")
    override val primaryKey = PrimaryKey(profileid)
}
