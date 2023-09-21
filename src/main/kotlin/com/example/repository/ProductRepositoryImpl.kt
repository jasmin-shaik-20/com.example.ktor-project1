package com.example.repository

import com.example.dao.ProductDao
import com.example.database.table.Products
import com.example.database.table.Users
import com.example.entities.ProductEntity
import com.example.entities.UserEntity
import com.example.exceptions.UserNotFoundException
import com.example.model.Product
import com.example.model.ProductInput
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToProduct
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ProductRepositoryImpl : ProductDao {
    override suspend fun createProduct(productInput: ProductInput): Product {
        return transaction {
            Users.select { Users.id eq UUID.fromString(productInput.userId) }.singleOrNull() ?:throw UserNotFoundException()

            val insert=Products.insert {
                it[userId]=UUID.fromString(productInput.userId)
                it[name]=productInput.name
                it[price]=productInput.price
            }
            val productId=UUID.fromString(insert[Products.id].toString())
            Product(productId.toString(),productInput.userId,productInput.name,productInput.price)
        }
    }

    override suspend fun getAllProducts(): List<Product> = dbQuery{
        Products.selectAll().map(::resultRowToProduct)
    }

    override suspend fun getProductByUserID(userId: UUID): List<Product> = dbQuery {
        Products.select { Products.userId eq userId }.map(::resultRowToProduct)
    }
    override suspend fun getProductById(productId: UUID): Product? = dbQuery {
        Products.select(Products.id eq productId).map(::resultRowToProduct).singleOrNull()
    }

    override suspend fun deleteProduct(productId: UUID): Boolean = dbQuery{
        val delProduct= Products.deleteWhere { Products.id eq id }
        delProduct>0
    }

    override suspend fun updateProduct(productId: UUID, newName: String, newPrice: Int): Boolean = dbQuery{
        val editProduct=Products.update ({ Products.id eq productId }){
            it[name]=newName
            it[price]=newPrice
        }
        editProduct>0
    }
}
