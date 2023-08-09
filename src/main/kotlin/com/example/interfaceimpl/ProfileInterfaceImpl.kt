package com.example.interfaceimpl

import com.example.dao.Profile
import com.example.dao.UserProfile
import com.example.dao.Users
import com.example.interfaces.ProfileInterface
import com.example.plugins.UserNotFoundException
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProfileInterfaceImpl : ProfileInterface {
    override suspend fun createUserProfile(profileId: Int, userId: Int, email: String, age: Int): UserProfile? =
        dbQuery {
            val user=Users.select { Users.id eq userId }.singleOrNull()?: throw UserNotFoundException()
            val profile = Profile.insert {
                it[Profile.userId] = userId
                it[Profile.email] = email
                it[Profile.age] = age
            }
            profile.resultedValues?.singleOrNull()?.let(::resultRowToProfile)
        }

    override suspend fun getAllUserProfile(): List<UserProfile> = dbQuery {
        Profile.selectAll().map(::resultRowToProfile)
    }


    override suspend fun getUserProfile(profileId: Int):UserProfile?= dbQuery {
        Profile.select(Profile.profileId eq profileId).map(::resultRowToProfile).singleOrNull()
    }

    override suspend fun getProfileByUserId(userId: Int):UserProfile? = dbQuery {
        Profile.select(Profile.userId eq userId).map(::resultRowToProfile).singleOrNull()
    }

    override suspend fun deleteUserProfile(profileId: Int): Boolean = dbQuery{
        val delProfile=Profile.deleteWhere {Profile.userId eq profileId  }
        delProfile>0
    }

    override suspend fun editUserProfile(profileId: Int, newEmail: String, newAge: Int): Boolean = dbQuery{
        val editProfile=Profile.update({Profile.profileId eq profileId}){
            it[email]=newEmail
            it[age]=newAge
        }
        editProfile>0
    }

    private fun resultRowToProfile(row: ResultRow) =
        UserProfile(
            profileId = row[Profile.profileId],
            userId = row[Profile.userId],
            email = row[Profile.email],
            age = row[Profile.age]
        )
}