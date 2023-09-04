package com.example.services

import com.example.database.table.User
import com.example.exceptions.UserCreationFailedException
import com.example.exceptions.UserInvalidNameLengthException
import com.example.exceptions.UserNotFoundException
import com.example.repository.UsersRepositoryImpl

class UserServices {

    private var usersRepository= UsersRepositoryImpl()
    suspend fun handleGetUsers():List<User> {
        val getUsers = usersRepository.getAllUsers()
        return if (getUsers.isEmpty()) {
            emptyList()
        } else {
            getUsers
        }
    }

    suspend fun handlePostUser(userDetails: User,
                               nameMinLength: Int?,
                               nameMaxLength: Int?
    ):User {
        if (userDetails.name.length in nameMinLength!!..nameMaxLength!!) {
            val user = usersRepository.createUser(userDetails.id,
                userDetails.name
            )?: throw UserCreationFailedException()
            return user
        } else {
            throw UserInvalidNameLengthException()
        }
    }

    suspend fun handleGetUserById(id: Int?): User {
        return usersRepository.selectUser(id!!) ?: throw UserNotFoundException()
    }

    suspend fun handleDeleteUser(id: Int):Boolean{
        val delUser= usersRepository.deleteUser(id)
        return if(delUser)
             true
        else {
            throw UserNotFoundException()
        }
    }

    suspend fun handleUpdateUser(id: Int, userDetails: User): Boolean {
            val isUpdated = usersRepository.editUser(id,userDetails.name)
        return if(isUpdated){
            true
        }else {
            throw UserNotFoundException()
        }
    }
}