package com.example.repository

import com.example.dao.UserDao
import com.example.database.table.Users
import com.example.model.User
import com.example.model.UserName
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToUser
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsersRepositoryImpl : UserDao {
    override suspend fun createUser(userName: UserName): User {
        return transaction {
            val insert = Users.insert {
                it[name] = userName.name
            }
            val userId = UUID.fromString(insert[Users.id].toString())
            User(userId.toString(),userName.name)
        }
    }

    override suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun getUserById(id: UUID): User? = dbQuery {
        Users
            .select(Users.id eq id)
            .map(::resultRowToUser)
            .firstOrNull()
    }

    override suspend fun deleteUser(id:UUID):Boolean = dbQuery{
        val delUser = Users.deleteWhere { Users.id eq id }
        delUser>0
    }

    override suspend fun updateUser(id:UUID, newName:String): Boolean = dbQuery {
        val editUser=Users.update ({ Users.id eq id }){
            it[name]=newName
        }
        editUser>0
    }


}

