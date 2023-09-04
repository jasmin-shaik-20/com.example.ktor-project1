package com.example.services

import com.example.dao.*
import com.example.database.table.*
import com.example.exceptions.CourseNameInvalidLengthException
import com.example.exceptions.CourseNotFoundException
import com.example.repository.CourseRepositoryImpl
import com.example.repository.StudentRepositoryImpl
import com.example.utils.H2Database
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import kotlin.test.assertFailsWith

class CourseServicesTest {

    private val courseRepositoryImpl=CourseRepositoryImpl()
    private val courseServices=CourseServices()
    private val studentRepositoryImpl=StudentRepositoryImpl()
    private lateinit var database: Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Students, Courses, StudentCourses)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Students,Courses,StudentCourses)
        }
    }

    //sucess
    @Test
    fun testHandleGetAllCourses() {
        runBlocking {
            val student1 = Student(1, "Jasmin")
            val student2 = Student(2, "Divya")
            studentRepositoryImpl.insertStudent(student1.id, student1.name)
            studentRepositoryImpl.insertStudent(student2.id, student2.name)
            val course1 = Course(1, student1.id, "Ktor")
            val course2 = Course(2, student1.id, "Kotlin")
            val course3 = Course(3, student2.id, "Java")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            courseRepositoryImpl.insertCourse(course2.studentId, course2.name)
            courseRepositoryImpl.insertCourse(course3.studentId, course3.name)
            val courses = courseServices.handleGetCourses()
            assertTrue(course1 in courses)
            assertTrue(course2 in courses)
            assertTrue(course3 in courses)
        }
    }

    @Test
    fun testHandlePostCourse() {
        runBlocking {
            val student = Student(1, "Jasmin")
            studentRepositoryImpl.insertStudent(student.id, student.name)
            val courseDetails = Course(1, student.id, "Ktor")
            val createdCourse = courseServices.handlePostCourse(courseDetails,3, 10)
            assertEquals(courseDetails, createdCourse)
        }
    }

    @Test
    fun testHandleGetCourseById(){
        runBlocking {
            val student = Student(1, "Jasmin")
            studentRepositoryImpl.insertStudent(student.id, student.name)
            val course1 = Course(1, student.id, "Ktor")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            val getCourse=courseServices.handleGetCourseById(course1.id)
            assertEquals(course1,getCourse)
        }
    }

    @Test
    fun testHandleDeleteCourse(){
        runBlocking {
            val student = Student(1, "Jasmin")
            studentRepositoryImpl.insertStudent(student.id, student.name)
            val course1 = Course(1, student.id, "Ktor")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            val delCourse=courseServices.handleDeleteCourse(course1.id)
            assertEquals(true,delCourse)
        }
    }

    @Test
    fun testHandleEditCourse(){
        runBlocking {
            val student = Student(1, "Jasmin")
            studentRepositoryImpl.insertStudent(student.id, student.name)
            val course1 = Course(1, student.id, "Ktor")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            val updatedCourse=Course(course1.id,course1.studentId,"Ktolin")
            val editCourses=courseServices.handlePutCourse(updatedCourse.id,updatedCourse)
            assertEquals(true,editCourses)
        }
    }

    //failure
    @Test
    fun testHandlePostCourseInvalidLength(){
        runBlocking {
            val course=Course(1,1,"python")
            assertFailsWith<CourseNameInvalidLengthException>("Invalid course name length"){
                courseServices.handlePostCourse(course,3,5)
            }
        }
    }

    @Test
    fun testHandleGetCourseNotFound(){
        runBlocking {
            val courseId=2
            assertFailsWith<CourseNotFoundException>("Course not found"){
                courseServices.handleGetCourseById(courseId)
            }
        }
    }

    @Test
    fun testHandleDeleteCourseNotFound(){
        runBlocking {
            val courseId=2
            assertFailsWith<CourseNotFoundException>("Course not found"){
                courseServices.handleDeleteCourse(courseId)
            }
        }
    }

    @Test
    fun testHandleEditCourseNotFound(){
        runBlocking {
            val course=Course(1,1,"Ktor")
            assertFailsWith<CourseNotFoundException>("Course not found"){
                courseServices.handlePutCourse(course.id,course)
            }
        }
    }


}