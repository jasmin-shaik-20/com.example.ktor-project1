package com.example.repository

import com.example.dao.StudentDao
import com.example.database.table.Students
import com.example.entities.StudentEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class StudentRepositoryImpl(id: EntityID<UUID>) : UUIDEntity(id), StudentDao {
    companion object : UUIDEntityClass<StudentRepositoryImpl>(Students)
    override suspend fun createStudent(name: String): StudentEntity {
        return transaction {
            val newStudent = StudentEntity.new {
                this.name = name
            }
            newStudent
        }
    }

    override fun getStudentById(studentId: UUID): StudentEntity? {
        return transaction {
            StudentEntity.findById(studentId)
        }
    }

    override fun getAllStudents(): List<StudentEntity> {
        return transaction {
            StudentEntity.all().toList()
        }
    }

    override fun updateStudent(studentId: UUID, name: String): Boolean {
        return transaction {
            val student = StudentEntity.findById(studentId)
            if (student != null) {
                student.name = name
                true
            } else {
                false
            }
        }
    }

    override fun deleteStudent(studentId: UUID): Boolean {
        return transaction {
            val student = StudentEntity.findById(studentId)
            if (student != null) {
                student.delete()
                true
            } else {
                false
            }
        }
    }
}
