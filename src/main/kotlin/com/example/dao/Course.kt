package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class Course(val id:Int=0,val studentId:Int,val name:String)

object Courses: Table("courses"){
    val id=integer("id").autoIncrement()
    val studentId=integer("studentId") references(Students.id)
    val name= varchar("name",100)
    override val primaryKey=PrimaryKey(id)

}