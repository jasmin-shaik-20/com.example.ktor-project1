import com.example.entities.UserEntity
import java.util.UUID

interface UserDao {
    fun createUser(name: String): UserEntity
    fun getUserById(id: UUID): UserEntity?
    fun getAllUsers(): List<UserEntity>
    fun updateUser(id: UUID, name: String): Boolean
    fun deleteUser(id: UUID): Boolean
}
