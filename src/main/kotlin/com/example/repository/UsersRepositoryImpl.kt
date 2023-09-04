package com.example.repository

import com.example.dao.UserDao
import com.example.database.table.User
import com.example.database.table.Users
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToUser
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UsersRepositoryImpl : UserDao {
    override suspend fun createUser(id:Int,name: String): User? = dbQuery {

        val insertStatement = Users.insert {
            it[Users.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun selectUser(id: Int): User? = dbQuery {
        Users
            .select(Users.id eq id)
            .map(::resultRowToUser)
            .firstOrNull()
    }

    override suspend fun deleteUser(id: Int):Boolean = dbQuery{
        val delUser = Users.deleteWhere { Users.id eq id }
        delUser>0
    }

    override suspend fun editUser(id: Int, newName:String): Boolean = dbQuery {
        val editUser=Users.update ({ Users.id eq id }){
            it[name]=newName
        }
        editUser>0
    }


}
