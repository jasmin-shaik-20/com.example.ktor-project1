package com.example.repository

import com.example.database.table.Profile
import com.example.utils.H2Database
import junit.framework.TestCase.*
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

class UserProfileRepositoryImplTest {
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
    fun testCreateUserProfileSuccess() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"John Doe")
        val newProfile = profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        assertEquals(1, newProfile?.userId)
        assertEquals("jasmin@123", newProfile?.email)
        assertEquals(21, newProfile?.age)
    }

    @Test
    fun testGetAllProfiles() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        usersRepository.createUser(2,"John")
        usersRepository.createUser(3,"Susan")
        val profileRepositoryImpl = ProfileRepositoryImpl()
        profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        profileRepositoryImpl.createUserProfile(2, "john@456", 25)
        profileRepositoryImpl.createUserProfile(3, "susan@789", 30)
        val allProfiles = profileRepositoryImpl.getAllUserProfile()
        assertEquals(3, allProfiles.size)
    }

    @Test
    fun testGetUserProfileSuccess() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val newProfile = profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        val retrievedProfile = profileRepositoryImpl.getUserProfile(newProfile!!.profileId)
        assertEquals(newProfile, retrievedProfile)
    }

    @Test
    fun testEditUserProfileSuccess() = runBlocking {
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val newProfile = profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        val editResult = profileRepositoryImpl.editUserProfile(newProfile!!.profileId, "jasmin@example.com", 22)
        assertTrue(editResult)
        val updatedProfile = profileRepositoryImpl.getUserProfile(newProfile.profileId)
        assertEquals("jasmin@example.com", updatedProfile?.email)
        assertEquals(22, updatedProfile?.age)
    }

    @Test
    fun testDeleteUserProfileSuccess() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val newProfile = profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        val deleteResult = profileRepositoryImpl.deleteUserProfile(newProfile!!.profileId)
        assertTrue(deleteResult)
        val deletedProfile = profileRepositoryImpl.getUserProfile(newProfile.profileId)
        assertNull(deletedProfile)
    }
    @Test
    fun testGetProfileByUserId() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val usersRepository = UsersRepositoryImpl()
        usersRepository.createUser(1,"Jasmin")
        val newProfile = profileRepositoryImpl.createUserProfile(1, "jasmin@123", 21)
        val retrievedProfile = profileRepositoryImpl.getProfileByUserId(newProfile!!.userId)
        assertEquals(newProfile, retrievedProfile)
    }

    //failure
    @Test
    fun testGetUserProfileNotFound() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val retrievedProfile = profileRepositoryImpl.getUserProfile(1)
        assertNull(retrievedProfile)
    }

    @Test
    fun testEditUserProfileNotFound() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val editResult = profileRepositoryImpl.editUserProfile(1, "jasmin@example.com", 22)
        assertFalse(editResult)
    }

    @Test
    fun testDeleteUserProfileNotFound() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val deleteResult = profileRepositoryImpl.deleteUserProfile(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testGetProfileByUserIdNotFound() = runBlocking {
        val profileRepositoryImpl = ProfileRepositoryImpl()
        val retrievedProfile = profileRepositoryImpl.getProfileByUserId(1)
        assertNull(retrievedProfile)
    }

}