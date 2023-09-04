package com.example.dao

import com.example.database.table.User

interface UserDao{

    suspend fun createUser(id:Int,name: String): User?

    suspend fun getAllUsers(): List<User>

    suspend fun selectUser(id: Int): User?

    suspend fun deleteUser(id: Int):Boolean

    suspend fun editUser(id: Int,newName:String): Boolean
}