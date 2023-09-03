package com.example.services

import com.example.dao.Products
import com.example.dao.Student
import com.example.dao.Students
import com.example.dao.Users
import com.example.plugins.StudentNotFoundException
import com.example.repository.StudentRepository
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
import kotlin.test.assertFailsWith

class StudentServicesTest {

    private val studentServices=StudentServices()
    private val studentRepository=StudentRepository()
    private lateinit var database:Database

    @Before
    fun setup() {
        database = H2Database.init()
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ

        transaction(database) {
            SchemaUtils.create(Students)
        }
    }

    @After
    fun tearDown() {
        transaction(database) {
            SchemaUtils.drop(Students)
        }
    }

    //sucess
    @Test
    fun testHandleGetAllStudents(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            val student2=Student(2,"Divya")
            studentRepository.insertStudent(student1.id,student1.name)
            studentRepository.insertStudent(student2.id,student2.name)
            val getStudents=studentServices.handleGetStudents()
            assertEquals(listOf(student1,student2),getStudents)
        }
    }

    @Test
    fun testHandlePostStudent(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            studentRepository.insertStudent(student1.id,student1.name)
            val studentDetails=Student(1,"Jasmin")
            studentServices.handlePostStudent(studentDetails,4,10)
            assertEquals(studentDetails.id,student1.id)
            assertEquals(studentDetails.name,student1.name)
        }
    }

    @Test
    fun testHandleGetStudentById(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            studentRepository.insertStudent(student1.id,student1.name)
            val getStudent=studentServices.handleGetStudentById(student1.id)
            assertEquals(student1.id,getStudent.id)
        }
    }

    @Test
    fun testHandleDeleteStudentById(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            studentRepository.insertStudent(student1.id,student1.name)
            val getStudent=studentServices.handleDeleteStudent(student1.id)
            assertEquals(true,getStudent)
        }
    }

    @Test
    fun testHandleEditStudent(){
        runBlocking {
            val student1=Student(1,"Jasmin")
            studentRepository.insertStudent(student1.id,student1.name)
            val editStudent=Student(1,"jas")
            val isUpdated=studentServices.handleUpdateStudent(editStudent.id,editStudent)
            assertEquals(true,isUpdated)
        }
    }

    @Test
    fun testHandlePostStudentInvalidLength(){
        runBlocking {
            val student=Student(1,"Shaik Jasmin")
            assertFailsWith<Exception>("Invalid name length"){
                studentServices.handlePostStudent(student,4,7)
            }
        }
    }

    @Test
    fun testHandleGetProductNotFound(){
        runBlocking {
            val studentId=2
            assertFailsWith<StudentNotFoundException>("Student not found"){
                studentServices.handleGetStudentById(studentId)
            }
        }
    }

    @Test
    fun testHandleDeleteProductNotFound(){
        runBlocking {
            val studentId=2
            assertFailsWith<StudentNotFoundException>("Student not found"){
                studentServices.handleDeleteStudent(studentId)
            }
        }
    }

    @Test
    fun testHandleEditProductNotFound(){
        runBlocking {
            val student=Student(1,"Jasmin")
            assertFailsWith<StudentNotFoundException>("Student not found"){
                studentServices.handleUpdateStudent(student.id,student)
            }
        }
    }
}