package com.example.interfaces

import com.example.dao.*

interface StudentInterface {
    suspend fun insertStudent(id: Int, name: String): Student?
    suspend fun getAllStudents():List<Student>
    suspend fun deleteStudent(id:Int):Boolean
    suspend fun editStudent(id:Int,newName: String):Boolean
    suspend fun getStudentById(id: Int): Student?
}

