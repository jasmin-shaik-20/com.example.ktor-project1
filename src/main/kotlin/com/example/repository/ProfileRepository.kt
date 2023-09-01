package com.example.repository

import com.example.dao.Profile
import com.example.dao.UserProfile
import com.example.dao.Users
import com.example.plugins.UserNotFoundException
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProfileRepository  {
    suspend fun createUserProfile(userId: Int, email: String, age: Int): UserProfile? =
        dbQuery {
            Users.select { Users.id eq userId }.singleOrNull()?: throw UserNotFoundException()
            val profile = Profile.insert {
                it[Profile.userId] = userId
                it[Profile.email] = email
                it[Profile.age] = age
            }
            profile.resultedValues?.singleOrNull()?.let(::resultRowToProfile)
        }

    suspend fun getAllUserProfile(): List<UserProfile> = dbQuery {
        Profile.selectAll().map(::resultRowToProfile)
    }


    suspend fun getUserProfile(profileId: Int):UserProfile?= dbQuery {
        Profile.select(Profile.profileId eq profileId).map(::resultRowToProfile).singleOrNull()
    }

    suspend fun getProfileByUserId(userId: Int):UserProfile? = dbQuery {
        Profile.select(Profile.userId eq userId).map(::resultRowToProfile).singleOrNull()
    }

    suspend fun deleteUserProfile(profileId: Int): Boolean = dbQuery{
        val delProfile=Profile.deleteWhere {Profile.userId eq profileId  }
        delProfile>0
    }

    suspend fun editUserProfile(profileId: Int, newEmail: String, newAge: Int): Boolean = dbQuery{
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