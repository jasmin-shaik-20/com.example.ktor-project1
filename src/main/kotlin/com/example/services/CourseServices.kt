package com.example.services

import com.example.database.table.Course
import com.example.exceptions.CourseCreationFailedException
import com.example.exceptions.CourseNameInvalidLengthException
import com.example.exceptions.CourseNotFoundException
import com.example.repository.CourseRepository

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
            return insert ?: throw CourseCreationFailedException()
        } else {
            throw CourseNameInvalidLengthException()
        }
    }

    suspend fun handleDeleteCourse(id: Int): Boolean {
        val delCourse = courseRepository.deleteCourse(id)
        return if (delCourse) {
            true
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handlePutCourse(id:Int,courseDetails: Course): Boolean {
        val editCourse = courseRepository.editCourse(id, courseDetails.name)
        return if (editCourse) {
            true
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handleGetCourseById(id: Int): Course {
        val fetchedCourse = courseRepository.getCourseById(id)
        return fetchedCourse ?: throw CourseNotFoundException()
    }
}
