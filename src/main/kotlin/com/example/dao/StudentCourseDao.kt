package com.example.dao

import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import com.example.model.Course
import com.example.model.Student
import java.util.*

interface StudentCourseDao {
    suspend fun getCoursesByStudentId(studentId: UUID): List<Course>
    suspend fun getStudentsByCourseId(courseId: UUID): List<Student>
}