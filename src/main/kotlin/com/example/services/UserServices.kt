import com.example.repository.UsersRepositoryImpl
import com.example.entities.UserEntity
import com.example.exceptions.UserInvalidNameLengthException
import com.example.exceptions.UserNotFoundException
import java.util.*

class UserServices(private val usersRepository: UsersRepositoryImpl) {

    fun handleGetUsers(): List<UserEntity> {
        return usersRepository.getAllUsers()
    }

    fun handlePostUser(userDetails: UserEntity,
                               nameMinLength: Int?,
                               nameMaxLength: Int?
    ): UserEntity {
        if (userDetails.name.length in nameMinLength!!..nameMaxLength!!) {
            return usersRepository.createUser(userDetails.name)
        } else {
            throw UserInvalidNameLengthException()
        }
    }

    fun handleGetUserById(id: UUID): UserEntity {
        return usersRepository.getUserById(id) ?: throw UserNotFoundException()
    }

    fun handleDeleteUser(id: UUID): Boolean {
        val delUser = usersRepository.deleteUser(id)
        return if (delUser) {
            true
        } else {
            throw UserNotFoundException()
        }
    }

    fun handleUpdateUser(id: UUID, userDetails: UserEntity): Boolean {
        val isUpdated = usersRepository.updateUser(id, userDetails.name)
        return if (isUpdated) {
            true
        } else {
            throw UserNotFoundException()
        }
    }
}
