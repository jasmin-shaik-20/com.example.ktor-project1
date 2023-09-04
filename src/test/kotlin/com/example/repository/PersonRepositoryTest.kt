package com.example.repository

import com.example.database.table.Persons
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import kotlin.test.assertEquals

class PersonRepositoryTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Persons)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Persons)
        }
    }

    @Test
    fun testCreatePersonData() = runBlocking {
        val personRepository = PersonRepository()
        val createdPerson = personRepository.createPersonData(1, "Alice")
        assertEquals("Alice", createdPerson?.name)
    }

    @Test
    fun testFetchDataExistingPerson() = runBlocking {
        val personRepository = PersonRepository()
        personRepository.createPersonData(1,"Alice")
        val fetchedData = personRepository.fetchData(1)
        assertEquals("Alice", fetchedData)
    }

    @Test
    fun testFetchDataNonExistingPerson() = runBlocking {
        val personRepository = PersonRepository()
        val fetchedData = personRepository.fetchData(1)
        assertEquals("Person not found", fetchedData)
    }
}
