package com.example.interfaces

import com.example.dao.Person
import com.example.dao.Persons
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface PersonInterface{
    suspend fun createPersonData(id:Int,name:String):Person?
    suspend fun fetchData(id:Int):String
}




