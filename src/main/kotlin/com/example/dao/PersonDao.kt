package com.example.dao

import com.example.entities.PersonEntity
import java.util.*

interface PersonDao {
    suspend fun createPerson(name: String): PersonEntity
    suspend fun getPersonById(id: UUID):String
}