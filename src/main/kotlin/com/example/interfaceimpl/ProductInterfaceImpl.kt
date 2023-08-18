package com.example.interfaceimpl

import com.example.dao.Product
import com.example.dao.Products
import com.example.dao.Users
import com.example.plugins.ProductInterface
import com.example.plugins.UserNotFoundException
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProductInterfaceImpl : ProductInterface {
    override suspend fun insertProduct(productId: Int, userId: Int, name: String, price: Int): Product? = dbQuery {
        Users.select { Users.id eq userId }.singleOrNull()?:throw UserNotFoundException()
        val insert = Products.insert {
            it[Products.userId] = userId
            it[Products.name] = name
            it[Products.price] = price
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToProduct)
    }

    override suspend fun getAllProducts(): List<Product> = dbQuery{
        Products.selectAll().map(::rowToProduct)
    }

    override suspend fun getProductsById(userId: Int): List<Product> = dbQuery {
        Products.select { Products.userId eq userId }.map(::rowToProduct)
    }
    override suspend fun getProduct(id: Int): Product? = dbQuery {
        Products.select(Products.productId eq id).map(::rowToProduct).singleOrNull()
    }

    override suspend fun deleteProduct(productId: Int): Boolean = dbQuery{
        val delProduct= Products.deleteWhere { Products.productId eq productId  }
        delProduct>0
    }

    override suspend fun editProduct(productId: Int, newName: String, newPrice: Int): Boolean = dbQuery{
        val editProduct=Products.update({Products.productId eq productId}){
            it[name]=newName
            it[price]=newPrice
        }
        editProduct>0
    }
}
private fun rowToProduct(row: ResultRow)=
    Product(row[Products.productId],row[Products.userId],row[Products.name],row[Products.price])
