package com.example.dao

import com.example.model.Student
import com.example.model.StudentInput
import java.util.UUID

interface StudentDao {
    suspend fun createStudent(studentInput: StudentInput): Student
    suspend fun getStudentById(studentId: UUID): Student?
    suspend fun getAllStudents(): List<Student>
    suspend fun updateStudent(studentId: UUID, newName: String): Boolean
    suspend fun deleteStudent(studentId: UUID): Boolean
}
