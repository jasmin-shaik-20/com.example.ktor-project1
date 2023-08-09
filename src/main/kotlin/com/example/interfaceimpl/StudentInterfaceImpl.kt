package com.example.interfaceimpl

import com.example.dao.Student
import com.example.dao.Students
import com.example.interfaces.StudentInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    override suspend fun deleteStudent(id: Int): Boolean = dbQuery {
        val delStudent =Students.deleteWhere{Students.id eq id}
        delStudent>0
    }

    override suspend fun editStudent(id: Int, newName: String): Boolean = dbQuery {
        val editStudent=Students.update({Students.id eq id}){
            it[name]=newName
        }
        editStudent>0
    }

    override suspend fun getStudentById(id: Int): Student? = dbQuery {
        Students.select { Students.id eq id }.map(::rowToStudent).singleOrNull()
    }
}

private fun rowToStudent(row: ResultRow) =
    Student(row[Students.id], row[Students.name])
