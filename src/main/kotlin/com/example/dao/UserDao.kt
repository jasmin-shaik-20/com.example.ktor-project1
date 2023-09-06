package com.example.dao

import com.example.entities.UserEntity
import com.example.model.User
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

interface UserDao {
    fun createUser(name: String): User?
    fun getUserById(id: UUID): User?
    fun getAllUsers(): List<User>
    fun updateUser(id: UUID, name: String): Boolean
    fun deleteUser(id: UUID): Boolean
}
