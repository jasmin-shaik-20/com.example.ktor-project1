package com.example.interfaces

import com.example.dao.*

interface StudentInterface {
    suspend fun insertStudent(id: Int, name: String): Student?
    suspend fun getAllStudents():List<Student>
    suspend fun getStudentById(id: Int): Student?
}

