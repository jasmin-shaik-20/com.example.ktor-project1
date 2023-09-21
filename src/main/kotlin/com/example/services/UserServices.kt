package com.example.services

import com.example.repository.UsersRepositoryImpl
import com.example.exceptions.UserInvalidNameLengthException
import com.example.exceptions.UserNotFoundException
import com.example.model.User
import com.example.model.UserName
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UserServices : KoinComponent {

    private val usersRepository by inject<UsersRepositoryImpl>()

    suspend fun handleGetUsers(): List<User> {
        return usersRepository.getAllUsers()
    }

    suspend fun handlePostUser(
        userName: UserName,
        nameMinLength: Int?,
        nameMaxLength: Int?
    ): User {
            if (userName.name.length in nameMinLength!!..nameMaxLength!!) {
                return usersRepository.createUser(userName)
            } else {
                throw UserInvalidNameLengthException()
            }
    }

    suspend fun handleGetUserById(id: UUID): User {
        return usersRepository.getUserById(id) ?: throw UserNotFoundException()
    }

    suspend fun handleDeleteUser(id: UUID): Boolean {
        val delUser = usersRepository.deleteUser(id)
        return if (delUser) {
            true
        } else {
            throw UserNotFoundException()
        }
    }

    suspend fun handleUpdateUser(id: UUID, userDetails: UserName): Boolean {
        val isUpdated = usersRepository.updateUser(id, userDetails.name)
        return if (isUpdated) {
            true
        } else {
            throw UserNotFoundException()
        }
    }
}
