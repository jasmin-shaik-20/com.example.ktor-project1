package com.example.routes

import com.example.dao.Product
import com.example.endpoints.ApiEndPoint
import com.example.interfaceimpl.ProductInterfaceImpl
import com.example.plugins.InvalidIDException
import com.example.plugins.ProductNotFoundException
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureProductRoutes(){
    val config = HoconApplicationConfig(ConfigFactory.load())
    val productNameMinLength= config.property("ktor.ProductValidation.productNameMinLength").getString()?.toIntOrNull()
    val productNameMaxLength= config.property("ktor.ProductValidation.productNameMaxLength").getString()?.toIntOrNull()
    routing{
        route(ApiEndPoint.PRODUCT){
            val productInterfaceImpl : ProductInterfaceImpl by inject()
            get{
                val getProducts= productInterfaceImpl.getAllProducts()
                if(getProducts.isEmpty()) {
                    throw ProductNotFoundException()
                }
                else{
                    call.application.environment.log.info("All product details")
                    call.respond(getProducts)
                }
            }

            post{
                val insert=call.receive<Product>()
                if(insert.name.length in productNameMinLength!!..productNameMaxLength!!) {
                    val postProduct = productInterfaceImpl
                        .insertProduct(insert.productId, insert.userId, insert.name, insert.price)
                    if (postProduct != null) {
                        call.application.environment.log.info("Product is created")
                        call.respond(HttpStatusCode.Created, postProduct)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                else{
                    call.respond("Invalid Length")
                }
            }

            get("/userId/{id?}"){
                val id= call.parameters["id"]?:throw  InvalidIDException()
                val getProduct=productInterfaceImpl.getProductsById(id.toInt())
                if(getProduct.isEmpty()){
                    call.application.environment.log.error("No product found with given id")
                    throw ProductNotFoundException()
                }
                else
                {
                    call.application.environment.log.info("Product list with given id is found")
                    call.respond(getProduct)
                }
            }

            get("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                val fetid=productInterfaceImpl.getProduct(id!!.toInt())
                if(fetid!=null){
                    call.application.environment.log.info("Product is found")
                    call.respond(fetid)
                }
                else{
                    throw ProductNotFoundException()
                }
            }

            delete("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val delProduct=productInterfaceImpl.deleteProduct(id)
                    if(delProduct){
                        call.application.environment.log.info("Product is deleted")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.application.environment.log.error("No product found with given id")
                        throw ProductNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id?}"){
                val id= call.parameters["id"]?.toIntOrNull()
                if(id!=null){
                    val product=call.receive<Product>()
                    val editProduct=productInterfaceImpl.editProduct(product.productId,product.name,product.price)
                    if(editProduct){
                        call.application.environment.log.info("Product is updated")
                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        throw ProductNotFoundException()
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest)
                }
            }


        }
    }
}