package com.example.repository

import com.example.dao.User
import com.example.dao.Users
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UsersRepository {
    suspend fun createUser(id:Int,name: String): User? = dbQuery {

        val insertStatement = Users.insert {
            it[Users.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    suspend fun selectUser(id: Int): User? = dbQuery {
        Users
            .select(Users.id eq id)
            .map(::resultRowToUser)
            .firstOrNull()
    }

    suspend fun deleteUser(id: Int):Boolean = dbQuery{
        val delUser = Users.deleteWhere { Users.id eq id }
        delUser>0
    }

    suspend fun editUser(id: Int,newName:String): Boolean = dbQuery {
        val editUser=Users.update ({ Users.id eq id }){
            it[name]=newName
        }
        editUser>0
    }
    private fun resultRowToUser(row: ResultRow) =
        User(id = row[Users.id], name = row[Users.name])

}
