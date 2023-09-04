package com.example.services

import com.example.database.table.Course
import com.example.database.table.Student
import com.example.repository.StudentCourseRepositoryImpl

class StudentCourseServices {
    private val studentCourseRepositoryImpl=StudentCourseRepositoryImpl()
    suspend fun handleGetCoursesByStudentId(studentId: Int): List<Course> {
        val courses = studentCourseRepositoryImpl.getCoursesStudentId(studentId)
        if (courses.isNotEmpty()) {
            return courses
        } else {
            throw NoSuchElementException("No courses found with the given student id")
        }
    }

    suspend fun handleGetStudentsByCourseId(courseId: Int): List<Student> {
        val students = studentCourseRepositoryImpl.getStudentsCourseId(courseId)
        if (students.isNotEmpty()) {
            return students
        } else {
            throw NoSuchElementException("No students found with the given course id")
        }
    }

}