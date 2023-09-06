package com.example.repository

import com.example.dao.UserDao
import com.example.database.table.Users
import com.example.entities.UserEntity
import com.example.model.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsersRepositoryImpl:UserDao {
    override fun createUser(name: String): User {
        return transaction {
            val newUserEntity = UserEntity.new {
                this.name = name
            }
            User(newUserEntity.id.value,newUserEntity.name)
        }
    }


    override fun getUserById(id: UUID): User?{
        return transaction {
           val user= UserEntity.findById(id)
            user?.let { User(it.id.value, it.name) }
        }
    }

    override fun getAllUsers(): List<User> {
        return transaction {
            UserEntity.all().map { User(it.id.value, it.name) }
        }
    }

    override fun updateUser(id: UUID, name: String): Boolean {
        return transaction {
            val userEntity = UserEntity.findById(id)
            if (userEntity != null) {
                userEntity.name = name
                true
            } else {
                false
            }
        }
    }

    override fun deleteUser(id: UUID): Boolean {
        return transaction {
            val userEntity = UserEntity.findById(id)
            if (userEntity != null) {
                userEntity.delete()
                true
            } else {
                false
            }
        }
    }
}
