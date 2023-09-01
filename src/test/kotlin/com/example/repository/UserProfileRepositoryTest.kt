package com.example.repository

import com.example.dao.Profile
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

class UserProfileRepositoryTest {
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
        val profileRepository = ProfileRepository()
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"John Doe")
        val newProfile = profileRepository.createUserProfile(1, "jasmin@123", 21)
        assertEquals(1, newProfile?.userId)
        assertEquals("jasmin@123", newProfile?.email)
        assertEquals(21, newProfile?.age)
    }

    @Test
    fun testGetAllProfiles() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        usersRepository.createUser(2,"John")
        usersRepository.createUser(3,"Susan")
        val profileRepository = ProfileRepository()
        profileRepository.createUserProfile(1, "jasmin@123", 21)
        profileRepository.createUserProfile(2, "john@456", 25)
        profileRepository.createUserProfile(3, "susan@789", 30)
        val allProfiles = profileRepository.getAllUserProfile()
        assertEquals(3, allProfiles.size)
    }

    @Test
    fun testGetUserProfileSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val profileRepository = ProfileRepository()
        val newProfile = profileRepository.createUserProfile(1, "jasmin@123", 21)
        val retrievedProfile = profileRepository.getUserProfile(newProfile!!.profileId)
        assertEquals(newProfile, retrievedProfile)
    }

    @Test
    fun testGetUserProfileNotFound() = runBlocking {
        val profileRepository = ProfileRepository()
        val retrievedProfile = profileRepository.getUserProfile(1)
        assertNull(retrievedProfile)
    }

    @Test
    fun testEditUserProfileSuccess() = runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val profileRepository = ProfileRepository()
        val newProfile = profileRepository.createUserProfile(1, "jasmin@123", 21)
        val editResult = profileRepository.editUserProfile(newProfile!!.profileId, "jasmin@example.com", 22)
        assertTrue(editResult)
        val updatedProfile = profileRepository.getUserProfile(newProfile.profileId)
        assertEquals("jasmin@example.com", updatedProfile?.email)
        assertEquals(22, updatedProfile?.age)
    }

    @Test
    fun testEditUserProfileNotFound() = runBlocking {
        val profileRepository = ProfileRepository()
        val editResult = profileRepository.editUserProfile(1, "jasmin@example.com", 22)
        assertFalse(editResult)
    }

    @Test
    fun testDeleteUserProfileSuccess() = runBlocking {
        val profileRepository = ProfileRepository()
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val newProfile = profileRepository.createUserProfile(1, "jasmin@123", 21)
        val deleteResult = profileRepository.deleteUserProfile(newProfile!!.profileId)
        assertTrue(deleteResult)
        val deletedProfile = profileRepository.getUserProfile(newProfile.profileId)
        assertNull(deletedProfile)
    }

    @Test
    fun testDeleteUserProfileNotFound() = runBlocking {
        val profileRepository = ProfileRepository()
        val deleteResult = profileRepository.deleteUserProfile(1)
        assertFalse(deleteResult)
    }

    @Test
    fun testGetProfileByUserId() = runBlocking {
        val profileRepository = ProfileRepository()
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"Jasmin")
        val newProfile = profileRepository.createUserProfile(1, "jasmin@123", 21)
        val retrievedProfile = profileRepository.getProfileByUserId(newProfile!!.userId)
        assertEquals(newProfile, retrievedProfile)
    }

    @Test
    fun testGetProfileByUserIdNotFound() = runBlocking {
        val profileRepository = ProfileRepository()
        val retrievedProfile = profileRepository.getProfileByUserId(1)
        assertNull(retrievedProfile)
    }

}