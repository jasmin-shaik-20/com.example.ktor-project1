package com.example.services

import com.example.database.table.Course
import com.example.database.table.Student
import com.example.repository.StudentCourseRepository

class StudentCourseServices {
    private val studentCourseRepository=StudentCourseRepository()
    suspend fun handleGetCoursesByStudentId(studentId: Int): List<Course> {
        val courses = studentCourseRepository.getCoursesStudentId(studentId)
        if (courses.isNotEmpty()) {
            return courses
        } else {
            throw NoSuchElementException("No courses found with the given student id")
        }
    }

    suspend fun handleGetStudentsByCourseId(courseId: Int): List<Student> {
        val students = studentCourseRepository.getStudentsCourseId(courseId)
        if (students.isNotEmpty()) {
            return students
        } else {
            throw NoSuchElementException("No students found with the given course id")
        }
    }

}