package com.example.dao
import kotlinx.serialization.Serializable
@Serializable
data class Login(val username:String,val password:String)