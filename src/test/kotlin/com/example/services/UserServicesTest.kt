
import com.example.database.table.Users
import com.example.entities.UserEntity
import com.example.exceptions.UserInvalidNameLengthException
import com.example.exceptions.UserNotFoundException
import com.example.services.UserServices
import com.example.utils.H2Database
import junit.framework.TestCase.assertNotNull
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserServicesTest( private val userServices : UserServices) {

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
            val user1 = UserEntity.new {
                name = "Alice"
            }
            val user2 = UserEntity.new {
                name = "Bob"
            }
            val getUsers = userServices.handleGetUsers()
            assertEquals(listOf(user1, user2), getUsers)
        }
    }

    @Test
    fun testHandlePostUser() {
        runBlocking {
            val userDetails = UserEntity.new {
                name = "Bob"
            }
            val createdUser = userServices.handlePostUser(userDetails, 3, 10)
            assertNotNull(createdUser)
            assertEquals(userDetails.id.value, createdUser.id.value)
            assertEquals(userDetails.name, createdUser.name)
        }
    }

    @Test
    fun testHandleGetUserById() {
        runBlocking {
            val user1 = UserEntity.new {
                name = "Alice"
            }
            val user = userServices.handleGetUserById(user1.id.value)
            assertEquals(user1.id.value, user.id.value)
        }
    }

    @Test
    fun testHandleDeleteUser() {
        runBlocking {
            val user1 = UserEntity.new {
                name = "Charlie"
            }
            val userDetails = UserEntity.new {
                name = "Charlie"
            }
            val isDeleted = userServices.handleDeleteUser(user1.id.value)
            assertEquals(true, isDeleted)
        }
    }

    @Test
    fun testHandleUpdateUser() {
        runBlocking {
            val user1 = UserEntity.new {
                name = "Charlie"
            }
            val userDetails = UserEntity.new {
                name = "UpdatedName"
            }
            val isUpdated = userServices.handleUpdateUser(user1.id.value, userDetails)
            assertEquals(true, isUpdated)
        }
    }


    //Failure
    @Test
    fun testHandlePostUserInvalidNameLength() {
        runBlocking {
            val userDetails = UserEntity.new {
                name = "David"
            }
            assertFailsWith<UserInvalidNameLengthException>("Invalid user name length") {
                userServices.handlePostUser(userDetails, 6, 10)
            }
        }
    }

    @Test
    fun testHandleGetUserByIdUserNotFound() {
        runBlocking {
            val userId = UUID.randomUUID()
            assertFailsWith<UserNotFoundException>("com.example.model.User not found") {
                userServices.handleGetUserById(userId)
            }
        }
    }

    @Test
    fun testHandleDeleteUserNotFound() {
        runBlocking {
            val userIdToDelete = UUID.randomUUID()
            assertFailsWith<UserNotFoundException>("com.example.model.User not found") {
                userServices.handleDeleteUser(userIdToDelete)
            }
        }
    }

    @Test
    fun testHandleUpdateUserNotFound() {
        runBlocking {
            val userIdToUpdate = UUID.randomUUID()
            val userDetails = UserEntity.new {
                name = "UpdatedName"
            }
            assertFailsWith<UserNotFoundException>("com.example.model.User not found") {
                userServices.handleUpdateUser(userIdToUpdate, userDetails)
            }
        }
    }
}
