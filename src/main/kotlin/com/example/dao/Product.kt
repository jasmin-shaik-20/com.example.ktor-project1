package com.example.dao

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class Product(val productId: Int, val userId: Int,val name:String, val price:Int)
object Products: Table("products"){
    val productId= integer("productId").autoIncrement()
    val userId=integer("userId").references(Users.id)
    val name=varchar("name",100)
    val price=integer("price")
    override val primaryKey = PrimaryKey(productId)
}








