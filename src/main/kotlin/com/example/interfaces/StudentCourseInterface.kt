package com.example.interfaces

import com.example.dao.Course
import com.example.dao.Student

interface StudentCourseInterface {
    suspend fun getCoursesStudentId(id: Int): List<Course>
    suspend fun getStudentsCourseId(id:Int): List<Student>
}