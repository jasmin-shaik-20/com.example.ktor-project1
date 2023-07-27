package com.example.plugins

import com.example.dao.User
import com.example.dao.UserProfile

interface ProfileInterface {
    suspend fun createUserProfile(profileid: Int, userid: Int, email: String, age: Int): UserProfile?
    suspend fun getUserProfile(profileid: Int): UserProfile?
}