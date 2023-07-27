package com.example.dao

import org.jetbrains.exposed.sql.Table

data class Product(val productid: Int, val userid: Int,val name:String, val price:Int)

object Products: Table("products"){
    val productid= integer("productid").autoIncrement()
    val userid=integer("userid").references(Users.id)
    val name=varchar("name",100)
    val price=integer("price")
    override val primaryKey = PrimaryKey(productid)
}








