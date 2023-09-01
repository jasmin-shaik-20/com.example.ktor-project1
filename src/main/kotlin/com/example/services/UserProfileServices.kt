package com.example.services

import com.example.dao.UserProfile
import com.example.plugins.UserProfileAlreadyExistFoundException
import com.example.plugins.UserProfileNotFoundException
import com.example.repository.ProfileRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
class UserProfileServices {

    private val profileRepository = ProfileRepository()

    suspend fun handleGetUserProfiles(): List<UserProfile> {
        val profiles = profileRepository.getAllUserProfile()
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
            val existingProfile = profileRepository.getProfileByUserId(userDetails.userId)
            if (existingProfile != null) {
                throw UserProfileAlreadyExistFoundException()
            }
            val profile = profileRepository.createUserProfile(
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
        val profile = profileRepository.getUserProfile(id!!)
            ?: throw UserProfileNotFoundException()
        return profile
    }

    suspend fun handleDeleteUserProfile(id: Int): Boolean {
        val deleted = profileRepository.deleteUserProfile(id)
        if (!deleted) {
            throw UserProfileNotFoundException()
        }
        return deleted
    }

    suspend fun handlePutUserProfile(id: Int, updatedProfile: UserProfile): Boolean {
        val updated = profileRepository.editUserProfile(id, updatedProfile.email, updatedProfile.age)
        if (!updated) {
            throw UserProfileNotFoundException()
        }
        return updated
    }
}
