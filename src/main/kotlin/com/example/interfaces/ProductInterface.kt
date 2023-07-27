package com.example.plugins

import com.example.dao.Product

interface ProductInterface {

    suspend fun insertProduct(productid:Int,userid:Int,name:String,price:Int): Product?

    suspend fun getProduct(id:Int):Product?
}