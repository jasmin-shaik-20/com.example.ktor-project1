package com.example.dao

import com.example.entities.CourseEntity
import com.example.model.Course
import com.example.model.CourseInput
import java.util.*

interface CourseDao {
    suspend fun createCourse(courseInput: CourseInput): Course?
    suspend fun getAllCourses(): List<Course>
    suspend fun deleteCourse(id: UUID): Boolean
    suspend fun updateCourse(id: UUID, newName: String): Boolean
    suspend fun getCourseById(id: UUID): Course?
}