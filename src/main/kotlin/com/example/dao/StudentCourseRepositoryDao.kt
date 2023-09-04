package com.example.dao

import com.example.database.table.Course
import com.example.database.table.Student

interface StudentCourseRepositoryDao {

    suspend fun getCoursesStudentId(id: Int): List<Course>

    suspend fun getStudentsCourseId(id: Int): List<Student>
}