package com.example.repository

import UserDao
import com.example.database.table.Users
import com.example.entities.UserEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsersRepositoryImpl( id: EntityID<UUID>) : UUIDEntity(id), UserDao {
    companion object : UUIDEntityClass<UsersRepositoryImpl>(Users)

    override fun createUser(name: String): UserEntity {
        return transaction {
            val newUser = UserEntity.new {
                this.name = name
            }
            newUser
        }
    }

    override fun getUserById(id: UUID): UserEntity? {
        return transaction {
            UserEntity.findById(id)
        }
    }

    override fun getAllUsers(): List<UserEntity> {
        return transaction {
            UserEntity.all().toList()
        }
    }

    override fun updateUser(id: UUID, name: String): Boolean {
        return transaction {
            val user = UserEntity.findById(id)
            if (user != null) {
                user.name = name
                true
            } else {
                false
            }
        }
    }

    override  fun deleteUser(id: UUID): Boolean {
        return transaction {
            val user = UserEntity.findById(id)
            if (user != null) {
                user.delete()
                true
            } else {
                false
            }
        }
    }
}
