import com.example.repository.UsersRepository
import com.example.dao.User
import com.example.dao.Users
import com.example.plugins.UserNotFoundException
import com.example.services.UserServices
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
import kotlin.test.assertFailsWith

class UserServicesTest {

    private val usersRepository = UsersRepository()
    private val userServices = UserServices()
    private lateinit var database: Database

    @Before
    fun setup() {
        database= H2Database.init()
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

    //Sucess
    @Test
    fun testHandleGetUsers() {
        runBlocking {
            val user1 = User(1, "Alice")
            val user2 = User(2, "Bob")
            usersRepository.createUser(user1.id, user1.name)
            usersRepository.createUser(user2.id, user2.name)
            val getUsers = userServices.handleGetUsers()
            assertEquals(listOf(user1, user2), getUsers)
        }
    }

    @Test
    fun testHandlePostUser() {
        runBlocking {
            val user1 = User(3, "Bob")
            usersRepository.createUser(user1.id, user1.name)
            val userDetails = User(3, "Bob")
            userServices.handlePostUser(userDetails, 3, 10)
            assertEquals(userDetails.id, user1.id)
            assertEquals(userDetails.name, user1.name)
        }
    }

    @Test
    fun testHandleGetUserById() {
        runBlocking {
            val user1 = User(1, "Alice")
            usersRepository.createUser(user1.id, user1.name)
            val user = userServices.handleGetUserById(1)
            assertEquals(user.id, user1.id)
        }
    }

    @Test
    fun testHandleDeleteUser() {
        runBlocking {
            val user1 = User(2, "Charlie")
            usersRepository.createUser(user1.id, user1.name)
            val userDetails = User(2, "Charlie")
            userServices.handlePostUser(userDetails, 3, 10)
            val isDeleted = userServices.handleDeleteUser(2)
            assertEquals(true, isDeleted)
        }
    }

    @Test
    fun testHandleUpdateUser() {
        runBlocking {
            val user1 = User(1, "Charlie")
            usersRepository.createUser(user1.id, user1.name)
            val userDetails = User(1, "UpdatedName")
            val isUpdated = userServices.handleUpdateUser(1, userDetails)
            assertEquals(true, isUpdated)
        }
    }

    //Failure

    @Test
    fun testHandlePostUserInvalidNameLength() {
        runBlocking {
            val userDetails = User(4, "David")
            assertFailsWith<Exception>("Invalid name length") {
                userServices.handlePostUser(userDetails, 6, 10)
            }
        }
    }

    @Test
    fun testHandleGetUserByIdUserNotFound() {
        runBlocking {
            val userId = 9999
            assertFailsWith<UserNotFoundException>("User not found") {
                userServices.handleGetUserById(userId)
            }
        }
    }

    @Test
    fun testHandleDeleteUserNotFound() {
        runBlocking {
            val userIdToDelete = 9999
            assertFailsWith<UserNotFoundException>("User not found") {
                userServices.handleDeleteUser(userIdToDelete)
            }
        }
    }

    @Test
    fun testHandleUpdateUserNotFound() {
        runBlocking {
            val userIdToUpdate = 9999
            val userDetails = User(userIdToUpdate, "UpdatedName")
            assertFailsWith<UserNotFoundException>("User not found") {
                userServices.handleUpdateUser(userIdToUpdate, userDetails)
            }
        }
    }
}
