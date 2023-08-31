package com.example.services

import com.example.dao.Profile
import com.example.dao.Users
import com.example.repository.ProfileRepository
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.sql.Connection
import kotlin.test.Test
import kotlin.test.assertEquals

class UserProfileTest {
    private lateinit var database: Database

    @Before
    fun setup() {
        database= H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Profile)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Profile)
        }
    }

    //success
    @Test
    fun testCreateUserProfileSuccess()= runBlocking {
        val profileRepository=ProfileRepository()
        val newProfile=profileRepository.createUserProfile(1,"jasmin@123",21)
        assertEquals(1,newProfile?.userId)
        assertEquals("jasmin@123",newProfile?.email)
        assertEquals(21,newProfile?.age)
    }

    @Test
    fun testGetAllProfiles()= runBlocking {

    }

}