package com.example.repository

import com.example.dao.PersonDao
import com.example.database.table.Person
import com.example.database.table.Persons
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.rowToPerson
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PersonRepository : PersonDao{

    override suspend fun createPersonData(id: Int, name: String): Person? = dbQuery {
        val insert= Persons.insert {
            it[Persons.name]=name
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToPerson)
    }

    override suspend fun fetchData(id: Int):String {
        val data= transaction {
            Persons.select { Persons.id eq id }.singleOrNull()
        }?.get(Persons.name) ?: "Person not found"
        return data
    }
}


