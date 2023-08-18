package com.example.interfaces

import com.example.dao.UserProfile

interface ProfileInterface {
    suspend fun createUserProfile(userId: Int, email: String, age: Int): UserProfile?
    suspend fun getAllUserProfile():List<UserProfile>
    suspend fun getUserProfile(profileId: Int): UserProfile?
    suspend fun getProfileByUserId(userId: Int):UserProfile?
    suspend fun deleteUserProfile(userId: Int):Boolean
    suspend fun editUserProfile(id:Int,newEmail:String,newAge:Int):Boolean
}
