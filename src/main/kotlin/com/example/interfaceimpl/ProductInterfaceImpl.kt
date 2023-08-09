package com.example.interfaceimpl

import com.example.dao.Product
import com.example.dao.Products
import com.example.plugins.ProductInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ProductInterfaceImpl : ProductInterface {
    override suspend fun insertProduct(productId: Int, userId: Int, name: String, price: Int): Product? = dbQuery {
        val insert = Products.insert {
            it[Products.userId] = userId
            it[Products.name] = name
            it[Products.price] = price
        }
        insert.resultedValues?.singleOrNull()?.let(::rowToProduct)
    }
    override suspend fun getProduct(id: Int): Product? = dbQuery {
        Products.select(Products.productId eq id).map(::rowToProduct).singleOrNull()
    }
}
private fun rowToProduct(row: ResultRow)=
    Product(row[Products.productId],row[Products.userId],row[Products.name],row[Products.price])