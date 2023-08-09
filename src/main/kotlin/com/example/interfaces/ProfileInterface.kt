package com.example.interfaces

import com.example.dao.UserProfile

interface ProfileInterface {
    suspend fun createUserProfile(profileId: Int, userId: Int, email: String, age: Int): UserProfile?
    suspend fun getAllUSerProfile():List<UserProfile>
    suspend fun getUserProfile(profileId: Int): UserProfile?
    suspend fun deleteUserProfile(profileId: Int):Boolean
    suspend fun editUserProfile(id:Int,newEmail:String,newAge:Int):Boolean
}