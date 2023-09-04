package com.example.dao

import com.example.database.table.Student

interface StudentDao {

    suspend fun insertStudent(id: Int, name: String): Student?

    suspend fun getAllStudents():List<Student>

    suspend fun deleteStudent(id: Int): Boolean

    suspend fun editStudent(id: Int, newName: String): Boolean

    suspend fun getStudentById(id: Int): Student?
}