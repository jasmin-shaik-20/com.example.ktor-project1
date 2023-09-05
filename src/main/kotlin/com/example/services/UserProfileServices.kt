package com.example.services

import com.example.repository.ProfileRepositoryImpl
import com.example.entities.UserProfileEntity
import com.example.exceptions.UserProfileInvalidEmailLengthException
import com.example.exceptions.UserProfileNotFoundException
import java.util.UUID

class UserProfileServices(private val profileRepositoryImpl: ProfileRepositoryImpl) {

    fun handleGetUserProfiles(): List<UserProfileEntity> {
       return profileRepositoryImpl.getAllUserProfiles()
    }

    fun handlePostUserProfile(
        userDetails: UserProfileEntity,
        emailMinLength: Int?,
        emailMaxLength: Int?
    ): UserProfileEntity {
        if (userDetails.email.length in emailMinLength!!..emailMaxLength!!) {
            return profileRepositoryImpl.createUserProfile(
                userDetails.userId.id.value,
                userDetails.email,
                userDetails.age
            )
        } else {
            throw UserProfileInvalidEmailLengthException()
        }
    }

    fun handleGetUserProfileById(id: UUID): UserProfileEntity {
        return profileRepositoryImpl.getUserProfileById(id)
            ?: throw UserProfileNotFoundException()
    }

    fun handleDeleteUserProfile(id: UUID): Boolean {
        val deleted = profileRepositoryImpl.deleteUserProfile(id)
        if (!deleted) {
            throw UserProfileNotFoundException()
        }
        return true
    }

    fun handlePutUserProfile(id:UUID, updatedProfile: UserProfileEntity): Boolean {
        val updated = profileRepositoryImpl.updateUserProfile(id, updatedProfile.email, updatedProfile.age)
        if (!updated) {
            throw UserProfileNotFoundException()
        }
        return true
    }
}
