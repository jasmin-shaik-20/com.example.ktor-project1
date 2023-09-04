package com.example.services

import com.example.database.table.Student
import com.example.exceptions.StudentCreationFailedException
import com.example.exceptions.StudentNameInvalidLengthException
import com.example.exceptions.StudentNotFoundException
import com.example.repository.StudentRepositoryImpl

class StudentServices {

    private val studentRepositoryImpl = StudentRepositoryImpl()

    suspend fun handleGetStudents(): List<Student> {
        val students = studentRepositoryImpl.getAllStudents()
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
            return studentRepositoryImpl.insertStudent(studentDetails.id, studentDetails.name)
                ?: throw StudentCreationFailedException()
        } else {
            throw StudentNameInvalidLengthException()
        }
    }

    suspend fun handleGetStudentById(id: Int?): Student {
        return studentRepositoryImpl.getStudentById(id!!) ?: throw StudentNotFoundException()
    }

    suspend fun handleDeleteStudent(id: Int): Boolean {
        val deleted = studentRepositoryImpl.deleteStudent(id)
        return if (deleted) {
            true
        } else {
            throw StudentNotFoundException()
        }
    }

    suspend fun handleUpdateStudent(id: Int, studentDetails: Student): Boolean {
        val isUpdated = studentRepositoryImpl.editStudent(id, studentDetails.name)
        return if (isUpdated) {
            true
        } else {
            throw StudentNotFoundException()
        }
    }
}
