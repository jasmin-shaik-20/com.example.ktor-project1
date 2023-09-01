package com.example.repository

import com.example.dao.Users
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

class UserRepositoryTest {

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
        val usersRepository = UsersRepository()
            val newUser = usersRepository.createUser(1,"John Doe")
            assertEquals(1, newUser?.id)
            assertEquals("John Doe", newUser?.name)
    }

    @Test
    fun testGetAllUsersSuccess() = runBlocking{
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"John Doe")
        val allUsers = usersRepository.getAllUsers()
        assertEquals(1, allUsers.size)
        assertEquals("John Doe", allUsers[0].name)
    }

    @Test
    fun testSelectUserSuccess()= runBlocking {
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"John Doe")
        val selectedUser = usersRepository.selectUser(1)
        assertEquals("John Doe", selectedUser?.name)
    }

    @Test
    fun testEditUserSuccess()= runBlocking{
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"John Doe")
        val editResult = usersRepository.editUser(1, "Jane Doe")
        assertTrue(editResult)
        val updatedUser = usersRepository.selectUser(1)
        assertEquals("Jane Doe", updatedUser?.name)
    }

    @Test
    fun testDeleteUserSuccess()= runBlocking{
        val usersRepository = UsersRepository()
        usersRepository.createUser(1,"John Doe")
        val deleteResult = usersRepository.deleteUser(1)
        assertTrue(deleteResult)
        val deletedUser = usersRepository.selectUser(1)
        assertNull(deletedUser)
    }

    //failure
    @Test
    fun testSelectUserNotFound()= runBlocking {
        val usersRepository = UsersRepository()
        val selectedUser = usersRepository.selectUser(1)
        assertNull(selectedUser)
    }

    @Test
    fun testEditUserNotFound()= runBlocking {
        val usersRepository = UsersRepository()
        val editResult = usersRepository.editUser(1, "Jane Doe")
        assertFalse(editResult)
    }

    @Test
    fun testDeleteUserNotFound()= runBlocking {
        val usersRepository = UsersRepository()
        val deleteResult = usersRepository.deleteUser(1)
        assertFalse(deleteResult)
    }
}


