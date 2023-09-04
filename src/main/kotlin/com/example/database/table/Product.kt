package com.example.database.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Product(val productId: Int=0, val userId: Int,val name:String, val price:Int)

object Products: Table("products"){
    private const val MAX_LENGTH=100
    val productId= integer("productId").autoIncrement()
    val userId=integer("userId").references(Users.id)
    val name=varchar("name",MAX_LENGTH)
    val price=integer("price")
    override val primaryKey = PrimaryKey(productId)
}









