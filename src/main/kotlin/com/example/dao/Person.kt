package com.example.dao

import org.jetbrains.exposed.sql.Table

data class Person(val id:Int,val name:String)

object Persons: Table(){
   val id=integer("id").autoIncrement()
   val name=varchar("name",100)
    override val primaryKey=PrimaryKey(id)
}