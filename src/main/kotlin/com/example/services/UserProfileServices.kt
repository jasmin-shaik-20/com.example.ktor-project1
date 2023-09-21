package com.example.services

import com.example.repository.ProfileRepositoryImpl
import com.example.entities.UserProfileEntity
import com.example.exceptions.UserNotFoundException
import com.example.exceptions.UserProfileInvalidEmailLengthException
import com.example.exceptions.UserProfileNotFoundException
import com.example.model.UserProfile
import com.example.model.UserProfileInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class UserProfileServices:KoinComponent {

    private val profileRepositoryImpl by inject<ProfileRepositoryImpl>()

    suspend fun handleGetUserProfiles(): List<UserProfile> {
       return profileRepositoryImpl.getAllUserProfiles()
    }

    suspend fun handlePostUserProfile(
        userProfileInput: UserProfileInput,
        emailMinLength: Int?,
        emailMaxLength: Int?
    ): UserProfile {
        if (userProfileInput.email.length in emailMinLength!!..emailMaxLength!!) {
            return profileRepositoryImpl.createUserProfile(userProfileInput)
        } else {
            throw UserProfileInvalidEmailLengthException()
        }
    }

    suspend fun handleGetUserProfileById(id: UUID): UserProfile {
        return profileRepositoryImpl.getUserProfileById(id)
            ?: throw UserProfileNotFoundException()
    }

    suspend fun handleGetUserProfileByUserID(userId:UUID):UserProfile{
        return profileRepositoryImpl.getUserProfileByUserId(userId)?:throw UserNotFoundException()
    }

    suspend fun handleDeleteUserProfile(id: UUID): Boolean {
        val deleted = profileRepositoryImpl.deleteUserProfile(id)
        if (!deleted) {
            throw UserProfileNotFoundException()
        }
        return true
    }

    suspend fun handlePutUserProfile(id:UUID, updatedProfile: UserProfileInput): Boolean {
        val updated = profileRepositoryImpl.updateUserProfile(id, updatedProfile.email, updatedProfile.age)
        if (!updated) {
            throw UserProfileNotFoundException()
        }
        return true
    }
}
