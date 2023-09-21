package com.example.repository

import com.example.dao.StudentDao
import com.example.database.table.Students
import com.example.entities.StudentEntity
import com.example.model.Student
import com.example.model.StudentInput
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToStudent
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class StudentRepositoryImpl : StudentDao {
    override suspend fun createStudent(studentInput: StudentInput): Student {
        return transaction {
            val insert=Students.insert {
                it[name]=studentInput.name
            }
            val studentId=UUID.fromString(insert[Students.id].toString())
            Student(studentId.toString(),studentInput.name)
        }
    }
    override suspend fun getAllStudents():List<Student> = dbQuery {
        Students.selectAll().map(::resultRowToStudent)
    }

    override suspend fun deleteStudent(studentId: UUID): Boolean = dbQuery {
        val delStudent =Students.deleteWhere { Students.id eq studentId }
        delStudent>0
    }

    override suspend fun updateStudent(studentId: UUID, newName: String): Boolean = dbQuery {
        val editStudent=Students.update({Students.id eq studentId}){
            it[name]=newName
        }
        editStudent>0
    }

    override suspend fun getStudentById(studentId: UUID): Student? = dbQuery {
        Students.select { Students.id eq studentId }.map(::resultRowToStudent).singleOrNull()
    }
}


