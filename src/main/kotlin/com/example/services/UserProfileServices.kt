package com.example.services

import com.example.database.table.UserProfile
import com.example.plugins.UserProfileNotFoundException
import com.example.repository.ProfileRepositoryImpl

class UserProfileServices {

    private val profileRepositoryImpl = ProfileRepositoryImpl()

    suspend fun handleGetUserProfiles(): List<UserProfile> {
        val profiles = profileRepositoryImpl.getAllUserProfile()
        if (profiles.isEmpty()) {
            return emptyList()
        } else {
            return profiles
        }
    }

    suspend fun handlePostUserProfile(
        userDetails: UserProfile,
        emailMinLength: Int?,
        emailMaxLength: Int?
    ): UserProfile {
        if (userDetails.email.length in emailMinLength!!..emailMaxLength!!) {
            val profile = profileRepositoryImpl.createUserProfile(
                userDetails.userId,
                userDetails.email,
                userDetails.age
            ) ?: throw Exception("User profile creation failed")
            return profile
        } else {
            throw Exception("Invalid email length")
        }
    }

    suspend fun handleGetUserProfileById(id: Int?): UserProfile {
        val profile = profileRepositoryImpl.getUserProfile(id!!)
            ?: throw UserProfileNotFoundException()
        return profile
    }

    suspend fun handleDeleteUserProfile(id: Int): Boolean {
        val deleted = profileRepositoryImpl.deleteUserProfile(id)
        if (!deleted) {
            throw UserProfileNotFoundException()
        }
        return deleted
    }

    suspend fun handlePutUserProfile(id: Int, updatedProfile: UserProfile): Boolean {
        val updated = profileRepositoryImpl.editUserProfile(id, updatedProfile.email, updatedProfile.age)
        if (!updated) {
            throw UserProfileNotFoundException()
        }
        return updated
    }
}
