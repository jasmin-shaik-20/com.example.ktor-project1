package com.example.interfaceimpl

import com.example.dao.Profile
import com.example.dao.UserProfile
import com.example.interfaces.ProfileInterface
import com.example.plugins.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ProfileInterfaceImpl : ProfileInterface {
    override suspend fun createUserProfile(profileId: Int, userId: Int, email: String, age: Int): UserProfile? =
        dbQuery {
            val profile = Profile.insert {
                it[Profile.userId] = userId
                it[Profile.email] = email
                it[Profile.age] = age
            }
            profile.resultedValues?.singleOrNull()?.let(::resultRowToProfile)
        }

    override suspend fun getUserProfile(profileId: Int):UserProfile?= dbQuery {
        Profile.select(Profile.profileId eq profileId).map(::resultRowToProfile).singleOrNull()
    }

    private fun resultRowToProfile(row: ResultRow) =
        UserProfile(
            profileId = row[Profile.profileId],
            userId = row[Profile.userId],
            email = row[Profile.email],
            age = row[Profile.age]
        )
}