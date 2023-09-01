package com.example.services

import com.example.dao.Student
import com.example.plugins.StudentNotFoundException
import com.example.repository.StudentRepository

class StudentServices {

    private val studentRepository = StudentRepository()

    suspend fun handleGetStudents(): List<Student> {
        val students = studentRepository.getAllStudents()
        return if (students.isEmpty()) {
            emptyList()
        } else {
            students
        }
    }

    suspend fun handlePostStudent(
        studentDetails: Student,
        studentNameMinLength: Int?,
        studentNameMaxLength: Int?
    ): Student {
        if (studentDetails.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
            val student = studentRepository.insertStudent(studentDetails.id, studentDetails.name)
                ?: throw Exception("Student creation failed")
            return student
        } else {
            throw Exception("Invalid name length")
        }
    }

    suspend fun handleGetStudentById(id: Int?): Student {
        return studentRepository.getStudentById(id!!) ?: throw StudentNotFoundException()
    }

    suspend fun handleDeleteStudent(id: Int): Boolean {
        val deleted = studentRepository.deleteStudent(id)
        return if (deleted) {
            deleted
        } else {
            throw StudentNotFoundException()
        }
    }

    suspend fun handleUpdateStudent(id: Int, studentDetails: Student): Boolean {
        val isUpdated = studentRepository.editStudent(id, studentDetails.name)
        return if (isUpdated) {
            isUpdated
        } else {
            throw StudentNotFoundException()
        }
    }
}
