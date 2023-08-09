package com.example.interfaces

import com.example.dao.Course

interface CourseInterface {
    suspend fun insertCourse( studentId: Int, name: String): Course?
    suspend fun getAllCourses():List<Course>
    suspend fun deleteCourse(id:Int):Boolean
    suspend fun editCourse(id:Int,newName:String):Boolean
    suspend fun getCourseById(id: Int): Course?
}