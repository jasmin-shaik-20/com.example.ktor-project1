package com.example.plugins

import com.example.config.DatabaseConfig.driver
import com.example.config.DatabaseConfig.password
import com.example.config.DatabaseConfig.url
import com.example.config.DatabaseConfig.user
import com.example.database.table.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabase() {

    Database.connect(url = url, driver = driver, user = user, password = password)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users, Profile)
        SchemaUtils.createMissingTablesAndColumns(Products)
        SchemaUtils.createMissingTablesAndColumns(Students, Courses,StudentCourses)
        SchemaUtils.createMissingTablesAndColumns(Persons)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
