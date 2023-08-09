package com.example.plugins

import com.example.dao.*
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/data",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "Jasmin@20"
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users, Profile)
        SchemaUtils.createMissingTablesAndColumns(Products)
        SchemaUtils.createMissingTablesAndColumns(Students,Courses,StudentCourses)
        SchemaUtils.createMissingTablesAndColumns(Persons)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }