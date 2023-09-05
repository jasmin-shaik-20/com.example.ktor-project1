package com.example.services

import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import com.example.repository.StudentCourseRepositoryImpl
import java.util.*
import kotlin.NoSuchElementException

class StudentCourseServices {
    private val studentCourseRepositoryImpl=StudentCourseRepositoryImpl()
    suspend fun handleGetCoursesByStudentId(studentId: UUID): List<CourseEntity> {
        val courses = studentCourseRepositoryImpl.getCoursesByStudentId(studentId)
        if (courses.isNotEmpty()) {
            return courses
        } else {
            throw NoSuchElementException("No courses found with the given student id")
        }
    }

    suspend fun handleGetStudentsByCourseId(courseId: UUID): List<StudentEntity> {
        val students = studentCourseRepositoryImpl.getStudentsByCourseId(courseId)
        if (students.isNotEmpty()) {
            return students
        } else {
            throw NoSuchElementException("No students found with the given course id")
        }
    }
}