package com.example.dao

import com.example.database.table.UserProfile

interface UserProfileDao {

    suspend fun createUserProfile(userId: Int, email: String, age: Int): UserProfile?

    suspend fun getAllUserProfile(): List<UserProfile>

    suspend fun getUserProfile(profileId: Int):UserProfile?

    suspend fun getProfileByUserId(userId: Int):UserProfile?

    suspend fun deleteUserProfile(profileId: Int): Boolean

    suspend fun editUserProfile(profileId: Int, newEmail: String, newAge: Int): Boolean
}