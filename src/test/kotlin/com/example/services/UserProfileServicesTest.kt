package com.example.services

import com.example.database.table.Profile
import com.example.database.table.User
import com.example.database.table.UserProfile
import com.example.database.table.Users
import com.example.exceptions.UserProfileInvalidEmailLengthException
import com.example.exceptions.UserProfileNotFoundException
import com.example.repository.ProfileRepositoryImpl
import com.example.repository.UsersRepositoryImpl
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
import kotlin.test.assertFailsWith

class UserProfileServicesTest {

    private val profileRepositoryImpl=ProfileRepositoryImpl()
    private val userProfileServices=UserProfileServices()
    private val usersRepository= UsersRepositoryImpl()
    private lateinit var database: Database

    @Before
    fun setup() {
        database= H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users, Profile)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Users,Profile)
        }
    }

    //sucess
    @Test
    fun testHandleGetAllProfile(){
        runBlocking {
            val user1 = User(1,"jasmin")
            val user2 = User(2,"Divya")
            usersRepository.createUser(user1.id,user1.name)
            usersRepository.createUser(user2.id,user2.name)
            val profile1 = UserProfile(1, user1.id, "jasmin@gmail.com", 21) // Use user1.id
            val profile2 = UserProfile(2, user2.id, "divya@gmail.com", 21) // Use user2.id
            profileRepositoryImpl.createUserProfile(profile1.userId, profile1.email, profile1.age)
            profileRepositoryImpl.createUserProfile(profile2.userId, profile2.email, profile2.age)
            val profiles = userProfileServices.handleGetUserProfiles()
            assertEquals(listOf(profile1, profile2), profiles)
        }
    }

    @Test
    fun testHandlePostProfile(){
        runBlocking {
            val user1 = User(1, "jasmin")
            usersRepository.createUser(user1.id, user1.name)
            val user2 = User(2,"Divya")
            usersRepository.createUser(user2.id,user2.name)
            val profile = UserProfile(1, user1.id, "jasmin@gmail.com", 21)
            profileRepositoryImpl.createUserProfile(profile.userId, profile.email, profile.age)
            val details = UserProfile(2, user2.id, "newemail@gmail.com", 30)
            val createdProfile = userProfileServices.handlePostUserProfile(details, 7, 20)
            assertEquals(details, createdProfile)
        }
    }

    @Test
    fun testHandleGetProfile(){
        runBlocking {
            val user1 =User(1,"jasmin")
            usersRepository.createUser(user1.id,user1.name)
            val profile=UserProfile(1,1,"jasmin@gmail.com",21)
            profileRepositoryImpl.createUserProfile(profile.userId,profile.email,profile.age)
            val getProfile=userProfileServices.handleGetUserProfileById(profile.profileId)
            assertEquals(profile,getProfile)
        }
    }

    @Test
    fun testHandleDeleteProfile(){
        runBlocking {
            val user1 =User(1,"jasmin")
            usersRepository.createUser(user1.id,user1.name)
            val profile=UserProfile(1,1,"jasmin@gmail.com",21)
            profileRepositoryImpl.createUserProfile(profile.userId,profile.email,profile.age)
            val deleteProfile=userProfileServices.handleDeleteUserProfile(profile.profileId)
            assertEquals(true,deleteProfile)

        }
    }

    @Test
    fun testHandleEditUserProfile(){
        runBlocking {
            val user1 =User(1,"jasmin")
            usersRepository.createUser(user1.id,user1.name)
            val profile=UserProfile(1,1,"jasmin@gmail.com",21)
            profileRepositoryImpl.createUserProfile(profile.userId,profile.email,profile.age)
            val updatedProfile=UserProfile(1,1,"jasmin123@gmail.com",23)
            val editProfile=userProfileServices.handlePutUserProfile(updatedProfile.profileId,updatedProfile)
            assertEquals(true,editProfile)
        }
    }

    @Test
    fun testHandlePostProfileInvalidLength(){
        runBlocking {
            val profile=UserProfile(1,1,"jasmin1234@gmail.com",21)
            assertFailsWith<UserProfileInvalidEmailLengthException>("Invalid email length"){
                userProfileServices.handlePostUserProfile(profile,4,7)
            }
        }
    }

    @Test
    fun testHandleGetProfileNotFound(){
        runBlocking {
            val profileId=2
            assertFailsWith<UserProfileNotFoundException>("UserProfile not found"){
                userProfileServices.handleGetUserProfileById(profileId)
            }
        }
    }

    @Test
    fun testHandleDeleteProfileNotFound(){
        runBlocking {
            val profileId=2
            assertFailsWith<UserProfileNotFoundException>("UserProfile not found"){
                userProfileServices.handleDeleteUserProfile(profileId)
            }
        }
    }

    @Test
    fun testHandleEditProfileNotFound(){
        runBlocking {
            val userId=1
            val profileId=2
            val profile=UserProfile(profileId,userId,"jas123@gmail.com",2)
            assertFailsWith<UserProfileNotFoundException>("UserProfile not found"){
                userProfileServices.handlePutUserProfile(profileId,profile)
            }
        }
    }

}