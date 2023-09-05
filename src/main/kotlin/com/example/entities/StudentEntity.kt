package com.example.entities

import com.example.database.table.Students
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class StudentEntity(id:EntityID<UUID>):UUIDEntity(id){
    companion object : UUIDEntityClass<StudentEntity>(Students)
    var name by Students.name
}