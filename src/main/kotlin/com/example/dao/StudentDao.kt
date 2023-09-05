package com.example.dao

import com.example.entities.StudentEntity
import java.util.UUID

interface StudentDao {
    suspend fun createStudent(name: String): StudentEntity
    fun getStudentById(studentId: UUID): StudentEntity?
    fun getAllStudents(): List<StudentEntity>
    fun updateStudent(studentId: UUID, name: String): Boolean
    fun deleteStudent(studentId: UUID): Boolean
}
