package com.example.plugins

import UsersInterface
import com.example.dao.User
import com.example.dao.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UsersInterfaceImpl : UsersInterface {
    override suspend fun createUser(id: Int, name: String): User? = dbQuery {

        val insertStatement = Users.insert {
            it[Users.name] = name

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)

    }

    override suspend fun selectUser(id: Int): User? = dbQuery {
        Users
            .select(Users.id eq id)
            .map(::resultRowToUser)
            .firstOrNull()

    }

    private fun resultRowToUser(row: ResultRow) =
        User(id = row[Users.id], name = row[Users.name])

}
