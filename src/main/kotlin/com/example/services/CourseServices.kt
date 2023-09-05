package com.example.services

import CourseRepositoryImpl
import com.example.entities.CourseEntity
import com.example.exceptions.CourseNameInvalidLengthException
import com.example.exceptions.CourseNotFoundException
import com.example.repository.ProfileRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class CourseServices : KoinComponent {

    private val courseRepositoryImpl by inject<CourseRepositoryImpl>()

    suspend fun handleGetCourses(): List<CourseEntity> {
        return courseRepositoryImpl.getAllCourses()
    }

    suspend fun handlePostCourse(
        courseDetails: CourseEntity,
        courseNameMinLength: Int?,
        courseNameMaxLength: Int?
    ): CourseEntity {
        if (courseDetails.name.length in courseNameMinLength!!..courseNameMaxLength!!) {
            return courseRepositoryImpl.createCourse(courseDetails.studentId.id.value, courseDetails.name)
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

    suspend fun handlePutCourse(id:UUID,courseDetails: CourseEntity): Boolean {
        val editCourse = courseRepositoryImpl.updateCourse(id, courseDetails.name)
        if (!editCourse) {
            throw CourseNotFoundException()
        }
        return true
    }

    suspend fun handleGetCourseById(id: UUID): CourseEntity {
        val fetchedCourse = courseRepositoryImpl.getCourseById(id)
        return fetchedCourse ?: throw CourseNotFoundException()
    }
}
