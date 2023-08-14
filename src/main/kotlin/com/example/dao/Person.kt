package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class Person(val id:Int=0,val name:String)
object Persons: Table(){
   val id=integer("id").autoIncrement()
   val name=varchar("name",100)
    override val primaryKey=PrimaryKey(id)
}