package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Student(val id:Int=0,val name:String)

object Students:Table("students"){
    private const val MAX_LENGTH=100
    val id=integer("id").autoIncrement()
    val name=varchar("name",MAX_LENGTH)
    override val primaryKey=PrimaryKey(id)
}


