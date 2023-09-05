package com.example.services

import com.example.entities.StudentEntity
import com.example.exceptions.StudentNameInvalidLengthException
import com.example.exceptions.StudentNotFoundException
import com.example.repository.StudentRepositoryImpl
import java.util.*

class StudentServices( private val studentRepositoryImpl : StudentRepositoryImpl) {

    fun handleGetStudents(): List<StudentEntity> {
        return studentRepositoryImpl.getAllStudents()
    }

    suspend fun handlePostStudent(
        studentDetails: StudentEntity,
        studentNameMinLength: Int?,
        studentNameMaxLength: Int?
    ): StudentEntity {
        if (studentDetails.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
            return studentRepositoryImpl.createStudent(studentDetails.name)
        } else {
            throw StudentNameInvalidLengthException()
        }
    }

    fun handleGetStudentById(id: UUID): StudentEntity {
        return studentRepositoryImpl.getStudentById(id) ?: throw StudentNotFoundException()
    }

    fun handleDeleteStudent(id: UUID): Boolean {
        val deleted = studentRepositoryImpl.deleteStudent(id)
        if(!deleted){
            throw StudentNotFoundException()
        }
        return true
    }

    fun handleUpdateStudent(id: UUID, studentDetails: StudentEntity): Boolean {
        val isUpdated = studentRepositoryImpl.updateStudent(id, studentDetails.name)
        if(!isUpdated){
            throw StudentNotFoundException()
        }
        return true
    }
}
