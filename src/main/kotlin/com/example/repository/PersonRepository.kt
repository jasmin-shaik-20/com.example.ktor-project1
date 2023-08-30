package com.example.repository

import com.example.dao.Person
import com.example.dao.Persons
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PersonRepository{

    suspend fun createPersonData(id: Int, name: String): Person? = dbQuery {
        val insert= Persons.insert {
            it[Persons.name]=name
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToPerson)
    }

    suspend fun fetchData(id: Int):String {
        val data= transaction {
            Persons.select { Persons.id eq id }.singleOrNull()
        }?.get(Persons.name) ?: "Person not found"
        return data
    }
}

private fun rowToPerson(row: ResultRow) =
    Person(row[Persons.id], row[Persons.name])
