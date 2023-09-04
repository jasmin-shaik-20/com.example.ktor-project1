package com.example.services

import com.example.database.table.*
import com.example.repository.CourseRepositoryImpl
import com.example.repository.StudentRepositoryImpl
import com.example.utils.H2Database
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection
import kotlin.test.assertEquals

class StudentCourseServicesTest {

    private val studentCourseServices=StudentCourseServices()
    private val studentRepositoryImpl= StudentRepositoryImpl()
    private val courseRepositoryImpl= CourseRepositoryImpl()
    private lateinit var database:Database

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
    fun testHandleGetCourseByStudentId(){
        runBlocking {
            val student1= Student(1,"Jasmin")
            studentRepositoryImpl.insertStudent(student1.id,student1.name)
            val course1 = Course(1, student1.id, "Ktor")
            val course2 = Course(2, student1.id, "Kotlin")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            courseRepositoryImpl.insertCourse(course2.studentId, course2.name)
            val getCourses=studentCourseServices.handleGetCoursesByStudentId(student1.id)
            assertEquals(listOf(course1,course2),getCourses)
        }
    }

    @Test
    fun testHandleGetStudentByCourseId(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            val student2=Student(2,"Divya")
            studentRepositoryImpl.insertStudent(student1.id,student1.name)
            studentRepositoryImpl.insertStudent(student2.id,student2.name)
            val course1 = Course(1, student1.id, "Ktor")
            courseRepositoryImpl.insertCourse(course1.studentId, course1.name)
            val getStudent=studentCourseServices.handleGetStudentsByCourseId(course1.studentId)
            assertEquals(listOf(student1),getStudent)
        }
    }


}