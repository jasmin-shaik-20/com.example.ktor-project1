package com.example.repository

import com.example.database.table.Users
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
import java.util.*
import kotlin.test.Test

class UserRepositoryTest(private val usersRepository: UsersRepositoryImpl) {

    private lateinit var database: Database

    @Before
    fun setup() {
        database=H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Users)
        }
    }

    //success
    @Test
    fun testCreateUserSuccess()=
        runBlocking {
            val newUser = usersRepository.createUser("John Doe")
            assertEquals(newUser.id.value, newUser.id)
            assertEquals("John Doe", newUser.name)
    }

    @Test
    fun testGetAllUsersSuccess() = runBlocking{
        val user=usersRepository.createUser("John Doe")
        val allUsers = usersRepository.getAllUsers()
        assertEquals(user.id.value, allUsers.size)
        assertEquals("John Doe", allUsers[0].name)
    }

    @Test
    fun testSelectUserSuccess()= runBlocking {
        val user=usersRepository.createUser("John Doe")
        val selectedUser = usersRepository.getUserById(user.id.value)
        assertEquals("John Doe", selectedUser?.name)
    }

    @Test
    fun testEditUserSuccess()= runBlocking{
        val user=usersRepository.createUser("John Doe")
        val editResult = usersRepository.updateUser(user.id.value, "Jane Doe")
        assertTrue(editResult)
        val updatedUser = usersRepository.getUserById(user.id.value)
        assertEquals("Jane Doe", updatedUser?.name)
    }

    @Test
    fun testDeleteUserSuccess()= runBlocking{
        val user=usersRepository.createUser("John Doe")
        val deleteResult = usersRepository.deleteUser(user.id.value)
        assertTrue(deleteResult)
        val deletedUser = usersRepository.getUserById(user.id.value)
        assertNull(deletedUser)
    }

    //failure
    @Test
    fun testSelectUserNotFound()= runBlocking {
        val userId = UUID.randomUUID()
        val selectedUser = usersRepository.getUserById(userId)
        assertNull(selectedUser)
    }

    @Test
    fun testEditUserNotFound()= runBlocking {
        val userId = UUID.randomUUID()
        val editResult = usersRepository.updateUser(userId, "Jane Doe")
        assertFalse(editResult)
    }

    @Test
    fun testDeleteUserNotFound()= runBlocking {
        val userId = UUID.randomUUID()
        val deleteResult = usersRepository.deleteUser(userId)
        assertFalse(deleteResult)
    }
}


