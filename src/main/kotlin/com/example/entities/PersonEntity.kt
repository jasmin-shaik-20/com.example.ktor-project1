package com.example.entities

import com.example.database.table.Persons
import com.example.database.table.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PersonEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PersonEntity>(Persons)
    var name by Users.name
}