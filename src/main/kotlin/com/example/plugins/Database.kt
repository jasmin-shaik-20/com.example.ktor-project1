package com.example.plugins

import com.example.dao.Students
import com.example.dao.Courses
import com.example.dao.Profile
import com.example.dao.Users
import com.example.dao.Products
import com.example.dao.StudentCourses
import com.example.dao.Persons
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.config.HoconApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val driver = config.property("ktor.database.driver").getString()
    val url = config.property("ktor.database.url").getString()
    val user = config.property("ktor.database.user").getString()
    val password = config.property("ktor.database.password").getString()
    Database.connect(url = url, driver = driver, user = user, password = password)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users, Profile)
        SchemaUtils.createMissingTablesAndColumns(Products)
        SchemaUtils.createMissingTablesAndColumns(Students,Courses,StudentCourses)
        SchemaUtils.createMissingTablesAndColumns(Persons)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
