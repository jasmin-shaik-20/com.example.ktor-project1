package com.example.dao

import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import java.util.*

interface StudentCourseDao {
    suspend fun getCoursesByStudentId(studentId: UUID): List<CourseEntity>
    suspend fun getStudentsByCourseId(courseId: UUID): List<StudentEntity>
}