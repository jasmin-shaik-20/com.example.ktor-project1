package com.example.services

import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import com.example.model.Course
import com.example.model.Student
import com.example.repository.StudentCourseRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.NoSuchElementException

class StudentCourseServices : KoinComponent {

    private val studentCourseRepositoryImpl by inject<StudentCourseRepositoryImpl>()
    suspend fun handleGetCoursesByStudentId(studentId: UUID): List<Course> {
        val courses = studentCourseRepositoryImpl.getCoursesByStudentId(studentId)
        if (courses.isNotEmpty()) {
            return courses
        } else {
            throw NoSuchElementException("No courses found with the given student id")
        }
    }

    suspend fun handleGetStudentsByCourseId(courseId: UUID): List<Student> {
        val students = studentCourseRepositoryImpl.getStudentsByCourseId(courseId)
        if (students.isNotEmpty()) {
            return students
        } else {
            throw NoSuchElementException("No students found with the given course id")
        }
    }
}