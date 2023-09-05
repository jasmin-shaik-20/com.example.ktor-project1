package com.example.repository

import com.example.dao.PersonDao
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import com.example.database.table.Persons
import com.example.entities.PersonEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PersonRepositoryImpl(id: EntityID<UUID>) : UUIDEntity(id), PersonDao {
    companion object : UUIDEntityClass<PersonRepositoryImpl>(Persons)

    override suspend fun createPerson(name: String): PersonEntity {
        return transaction {
            val newPerson = PersonEntity.new{
                this.name = name
            }
            newPerson
        }
    }

    override suspend fun getPersonById(id: UUID): String {
        return transaction {
            val person = PersonEntity.findById(id)
            person?.name ?: throw NoSuchElementException("Person not found with ID: $id")
        }
    }
}
