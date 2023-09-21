package com.example.dao

import com.example.entities.UserEntity
import com.example.model.User
import com.example.model.UserName
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

interface UserDao{

    suspend fun createUser(userName: UserName): User

    suspend fun getAllUsers(): List<User>

    suspend fun getUserById(id: UUID): User?

    suspend fun deleteUser(id: UUID):Boolean

    suspend fun updateUser(id: UUID,newName:String): Boolean
}