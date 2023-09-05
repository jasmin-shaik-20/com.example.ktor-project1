package com.example.entities

import com.example.database.table.Courses
import com.example.database.table.Students
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class CourseEntity(id:EntityID<UUID>):UUIDEntity(id) {
    companion object:UUIDEntityClass<CourseEntity>(Courses)
    var studentId by StudentEntity referencedOn Students.id
    var name by Courses.name
}