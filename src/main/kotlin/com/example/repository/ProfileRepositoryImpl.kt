package com.example.repository

import com.example.dao.UserProfileDao
import com.example.database.table.Profile
import com.example.database.table.UserProfile
import com.example.database.table.Users
import com.example.plugins.UserNotFoundException
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToProfile
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProfileRepositoryImpl : UserProfileDao{
    override suspend fun createUserProfile(userId: Int, email: String, age: Int): UserProfile? =
        dbQuery {
            Users.select { Users.id eq userId }.singleOrNull()?: throw UserNotFoundException()
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


}
