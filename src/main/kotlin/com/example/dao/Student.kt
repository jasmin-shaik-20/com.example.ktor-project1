package com.example.dao

import org.jetbrains.exposed.sql.Table

data class student(val id:Int,val name:String)

data class course(val id:Int=1,val student_id:Int,val name:String)


object students:Table("students"){
    val id=integer("id").autoIncrement()
    val name=varchar("name",100)
    override val primaryKey=PrimaryKey(id)
}

object courses:Table("courses"){
    val id=integer("id").autoIncrement()
    val student_id=integer("student_id") references(students.id)
    val name= varchar("name",100)
    override val primaryKey=PrimaryKey(id)

}

object studentcourses:Table("studentcourses"){
    val studentid=integer("studentid") references(students.id)
    val courseid=integer("courseid") references(courses.id)
    override val primaryKey=PrimaryKey(studentid, courseid)
}
