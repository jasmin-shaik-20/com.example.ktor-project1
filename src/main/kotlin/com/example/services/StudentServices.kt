package com.example.services

import com.example.entities.StudentEntity
import com.example.exceptions.StudentNameInvalidLengthException
import com.example.exceptions.StudentNotFoundException
import com.example.model.Student
import com.example.model.StudentInput
import com.example.repository.ProfileRepositoryImpl
import com.example.repository.StudentRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class StudentServices:KoinComponent {

    private val studentRepositoryImpl by inject<StudentRepositoryImpl>()

    suspend fun handleGetStudents(): List<Student> {
        return studentRepositoryImpl.getAllStudents()
    }

    suspend fun handlePostStudent(
        studentInput: StudentInput,
        studentNameMinLength: Int?,
        studentNameMaxLength: Int?
    ): Student {
        if (studentInput.name.length in studentNameMinLength!!..studentNameMaxLength!!) {
            return studentRepositoryImpl.createStudent(studentInput)
        } else {
            throw StudentNameInvalidLengthException()
        }
    }

    suspend fun handleGetStudentById(id: UUID): Student {
        return studentRepositoryImpl.getStudentById(id) ?: throw StudentNotFoundException()
    }

    suspend fun handleDeleteStudent(id: UUID): Boolean {
        val deleted = studentRepositoryImpl.deleteStudent(id)
        if(!deleted){
            throw StudentNotFoundException()
        }
        return true
    }

    suspend fun handleUpdateStudent(id: UUID, studentDetails: Student): Boolean {
        val isUpdated = studentRepositoryImpl.updateStudent(id, studentDetails.name)
        if(!isUpdated){
            throw StudentNotFoundException()
        }
        return true
    }
}
