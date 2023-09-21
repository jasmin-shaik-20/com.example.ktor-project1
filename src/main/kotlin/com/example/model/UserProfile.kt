package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(val id:String,val userId:String,val email:String,val age:Int)

@Serializable
data class UserProfileInput(val userId:String,val email:String,val age:Int)
