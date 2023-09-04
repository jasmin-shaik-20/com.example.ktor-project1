package com.example.services

import com.example.database.table.UserProfile
import com.example.exceptions.UserProfileCreationFailedException
import com.example.exceptions.UserProfileInvalidEmailLengthException
import com.example.exceptions.UserProfileNotFoundException
import com.example.repository.ProfileRepositoryImpl

class UserProfileServices {

    private val profileRepositoryImpl = ProfileRepositoryImpl()

    suspend fun handleGetUserProfiles(): List<UserProfile> {
        val profiles = profileRepositoryImpl.getAllUserProfile()
        return if (profiles.isEmpty()) {
            emptyList()
        } else {
            profiles
        }
    }

    suspend fun handlePostUserProfile(
        userDetails: UserProfile,
        emailMinLength: Int?,
        emailMaxLength: Int?
    ): UserProfile {
        if (userDetails.email.length in emailMinLength!!..emailMaxLength!!) {
            return profileRepositoryImpl.createUserProfile(
                userDetails.userId,
                userDetails.email,
                userDetails.age
            ) ?: throw UserProfileCreationFailedException()
        } else {
            throw UserProfileInvalidEmailLengthException()
        }
    }

    suspend fun handleGetUserProfileById(id: Int?): UserProfile {
        return profileRepositoryImpl.getUserProfile(id!!)
            ?: throw UserProfileNotFoundException()
    }

    suspend fun handleDeleteUserProfile(id: Int): Boolean {
        val deleted = profileRepositoryImpl.deleteUserProfile(id)
        if (!deleted) {
            throw UserProfileNotFoundException()
        }
        return true
    }

    suspend fun handlePutUserProfile(id: Int, updatedProfile: UserProfile): Boolean {
        val updated = profileRepositoryImpl.editUserProfile(id, updatedProfile.email, updatedProfile.age)
        if (!updated) {
            throw UserProfileNotFoundException()
        }
        return true
    }
}
