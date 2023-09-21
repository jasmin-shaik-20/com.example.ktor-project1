package com.example.services

import CourseRepositoryImpl
import com.example.entities.CourseEntity
import com.example.exceptions.CourseNameInvalidLengthException
import com.example.exceptions.CourseNotFoundException
import com.example.model.Course
import com.example.model.CourseInput
import com.example.repository.ProfileRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CourseServices : KoinComponent {

    private val courseRepositoryImpl by inject<CourseRepositoryImpl>()

    suspend fun handleGetCourses(): List<Course> {
        return courseRepositoryImpl.getAllCourses()
    }

    suspend fun handlePostCourse(
        courseInput: CourseInput,
        courseNameMinLength: Int?,
        courseNameMaxLength: Int?
    ): Course {
        if (courseInput.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
            return courseRepositoryImpl.createCourse(courseInput)
        } else {
            throw CourseNameInvalidLengthException()
        }
    }

    suspend fun handleDeleteCourse(id: UUID): Boolean {
        val delCourse = courseRepositoryImpl.deleteCourse(id)
        if (!delCourse) {
            throw CourseNotFoundException()
        }
        return true
    }

    suspend fun handlePutCourse(id:UUID,courseDetails: Course): Boolean {
        val editCourse = courseRepositoryImpl.updateCourse(id, courseDetails.name)
        if (!editCourse) {
            throw CourseNotFoundException()
        }
        return true
    }

    suspend fun handleGetCourseById(id: UUID): Course {
        val fetchedCourse = courseRepositoryImpl.getCourseById(id)
        return fetchedCourse ?: throw CourseNotFoundException()
    }
}
