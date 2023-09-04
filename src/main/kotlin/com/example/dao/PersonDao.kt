package com.example.dao

import com.example.database.table.Person

interface PersonDao {

    suspend fun createPersonData(id: Int, name: String): Person?

    suspend fun fetchData(id: Int):String
}