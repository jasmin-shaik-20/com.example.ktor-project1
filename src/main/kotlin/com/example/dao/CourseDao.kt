package com.example.dao

import com.example.entities.CourseEntity
import java.util.*

interface CourseDao {
    suspend fun createCourse(studentId: UUID, name: String): CourseEntity?
    suspend fun getAllCourses(): List<CourseEntity>
    suspend fun deleteCourse(id: UUID): Boolean
    suspend fun updateCourse(id: UUID, newName: String): Boolean
    suspend fun getCourseById(id: UUID): CourseEntity?
}