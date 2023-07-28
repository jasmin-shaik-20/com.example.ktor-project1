package com.example.interfaces

import com.example.dao.course
import com.example.dao.courses
import com.example.dao.student

interface StudentInterface {

    suspend fun insertStudent(id: Int, name: String): student?

    suspend fun getAllStudents():List<student>

    suspend fun getStudentById(id: Int): student?
}

interface CourseInterface {

    suspend fun insertCourse(id: Int, student_id: Int, name: String): course?

    suspend fun getAllCourses():List<course>

    suspend fun getCourseById(id: Int): course?
}

interface StudentCourseInterface {

    suspend fun getCoursesBystudentId(id: Int): List<course>

    suspend fun getStudentsBycourseId(id:Int): List<student>

}