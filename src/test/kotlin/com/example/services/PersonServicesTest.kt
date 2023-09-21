package com.example.services

import com.example.database.table.Person
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import redis.clients.jedis.Jedis
import java.sql.Connection
import kotlin.test.assertEquals

class PersonServicesTest {

    private val personRepositoryImpl = PersonRepositoryImpl()
    private val personServices = PersonServices()
    private lateinit var database:Database

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
    fun testHandlePostPersonDetails() {
        runBlocking {
            val person1=Person(1,"Jasmin")
            personRepositoryImpl.createPersonData(person1.id,person1.name)
            val person=Person(1,"Jasmin")
            personServices.handlePostPersonDetails(person)
            assertEquals(person1.id,person.id)
            assertEquals(person1.name,person.name)
        }

    }


    @Test
    fun testHandleGetDataFromCacheOrSourceWithDataFromSource() {
        runBlocking {
            val person1= Person(1,"Jasmin")
            personRepositoryImpl.createPersonData(person1.id,person1.name)
            val jedis = Jedis()
            val id = 1
            jedis.get("my_cached_data")
            val getData=personRepositoryImpl.fetchData(id)
            val response=personServices.handleGetDataFromCacheOrSource(personRepositoryImpl, jedis, id)
            assertEquals("Data from source: $getData", response)
        }
    }

}
