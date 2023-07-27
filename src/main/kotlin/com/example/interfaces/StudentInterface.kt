package com.example.interfaces

import com.example.dao.course
import com.example.dao.student

interface StudentInterface {

    suspend fun insertStudent(id:Int,name:String):student?

    suspend fun getAllStudents()

    suspend fun getStudentById(id:Int):student?
}

interface CourseInterface {

    suspend fun insertCourse(id:Int,student_id:Int,name:String): course?

    suspend fun getAllCourses()

    suspend fun getCourseById(id:Int):course?
}

