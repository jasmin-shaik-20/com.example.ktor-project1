package com.example.services

import com.example.database.table.Course
import com.example.exceptions.CourseCreationFailedException
import com.example.exceptions.CourseNameInvalidLengthException
import com.example.exceptions.CourseNotFoundException
import com.example.repository.CourseRepositoryImpl

class CourseServices {

    private val courseRepositoryImpl = CourseRepositoryImpl()

    suspend fun handleGetCourses(): List<Course> {
        val courses = courseRepositoryImpl.getAllCourses()
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
            val insert = courseRepositoryImpl.insertCourse(courseDetails.studentId, courseDetails.name)
            return insert ?: throw CourseCreationFailedException()
        } else {
            throw CourseNameInvalidLengthException()
        }
    }

    suspend fun handleDeleteCourse(id: Int): Boolean {
        val delCourse = courseRepositoryImpl.deleteCourse(id)
        return if (delCourse) {
            true
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handlePutCourse(id:Int,courseDetails: Course): Boolean {
        val editCourse = courseRepositoryImpl.editCourse(id, courseDetails.name)
        return if (editCourse) {
            true
        } else {
            throw CourseNotFoundException()
        }
    }

    suspend fun handleGetCourseById(id: Int): Course {
        val fetchedCourse = courseRepositoryImpl.getCourseById(id)
        return fetchedCourse ?: throw CourseNotFoundException()
    }
}
