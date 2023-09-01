package com.example.services

import com.example.dao.Course
import com.example.plugins.CourseNotFoundException
import com.example.repository.CourseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class CourseServices {

    private val courseRepository = CourseRepository()

    suspend fun handleGetCourses(): List<Course> {
        val courses = courseRepository.getAllCourses()
        return if (courses.isEmpty()) {
            emptyList()
        } else {
            courses
        }
    }

    suspend fun handlePostCourse(
        courseDetails: Course,
        courseNameMinLength: Int?,
        courseNameMaxLength: Int?
    ): Course {
        if (courseDetails.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
            val insert = courseRepository.insertCourse(courseDetails.studentId, courseDetails.name)
            return insert ?: throw Exception("Course creation failed")
        } else {
            throw Exception("Invalid Length")
        }
    }

    suspend fun handleDeleteCourse(id: Int): Boolean {
        val delCourse = courseRepository.deleteCourse(id)
        return if (delCourse) {
            delCourse
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handlePutCourse(id:Int,courseDetails: Course): Boolean {
        val editCourse = courseRepository.editCourse(id, courseDetails.name)
        return if (editCourse) {
            editCourse
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handleGetCourseById(id: Int): Course {
        val fetchedCourse = courseRepository.getCourseById(id)
        return fetchedCourse ?: throw CourseNotFoundException()
    }
}
