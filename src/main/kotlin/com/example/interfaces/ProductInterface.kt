package com.example.plugins

import com.example.dao.Product

interface ProductInterface {
    suspend fun insertProduct(productId:Int,userId:Int,name:String,price:Int): Product?
    suspend fun getProduct(id:Int):Product?
}