package com.example.interfaceimpl

import com.example.dao.Student
import com.example.dao.Students
import com.example.interfaces.StudentInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class StudentInterfaceImpl : StudentInterface {
    override suspend fun insertStudent(id: Int, name: String): Student? = dbQuery {
        val insert = Students.insert {
            it[Students.name] = name
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToStudent)
    }
    override suspend fun getAllStudents():List<Student> = dbQuery {
        Students.selectAll().map(::rowToStudent)
    }

    override suspend fun getStudentById(id: Int): Student? = dbQuery {
        Students.select { Students.id eq id }.map(::rowToStudent).singleOrNull()
    }
}

private fun rowToStudent(row: ResultRow) =
    Student(row[Students.id], row[Students.name])
