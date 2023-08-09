package com.example.interfaces

import com.example.dao.UserProfile

interface ProfileInterface {
    suspend fun createUserProfile(profileId: Int, userId: Int, email: String, age: Int): UserProfile?
    suspend fun getUserProfile(profileId: Int): UserProfile?
}