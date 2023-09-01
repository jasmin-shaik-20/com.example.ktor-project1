package com.example.utils

import org.jetbrains.exposed.sql.Database

object H2Database{
    private lateinit var database:Database
    fun init() :Database{
        database=Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
            user = "jasmin",
            password = "password"
        )
        return database
    }

}