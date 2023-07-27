package com.example.dao
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import java.io.Serial



data class User(val id:Int,val name:String)

object Users: Table("users_table"){
    val id=integer("id").autoIncrement()
    val name=varchar("name",100)
    override  val primaryKey=PrimaryKey(id)

}