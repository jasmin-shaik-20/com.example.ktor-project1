package com.example.interfaces

import com.example.dao.Person
import com.example.dao.Persons
import com.example.dao.student
import com.example.dao.students
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface PersonInterface{

    suspend fun createPersonData(id:Int,name:String)

    suspend fun fetchData(id:Int):String
}

class PersonInterfaceImpl:PersonInterface{

    override suspend fun createPersonData(id: Int, name: String) {
        transaction {
            Persons.insert {
                it[Persons.name] = name
            }
        }
    }

    override suspend fun fetchData(id: Int):String {
        val data= transaction {
            Persons.select { Persons.id eq id }.singleOrNull()
        }?.get(Persons.name) ?: "Person not found"
        return data
    }


}


//private fun rowToPerson(row: ResultRow) =
//    Person(row[Persons.id], row[Persons.name])



