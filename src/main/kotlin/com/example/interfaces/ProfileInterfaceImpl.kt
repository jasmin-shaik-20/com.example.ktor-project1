package com.example.plugins

import com.example.dao.Profile
import com.example.dao.UserProfile
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class ProfileInterfaceImpl : ProfileInterface {
    override suspend fun createUserProfile(profileid: Int, userid: Int, email: String, age: Int): UserProfile? =
        dbQuery {
            val profile = Profile.insert {
                it[Profile.userid] = userid
                it[Profile.email] = email
                it[Profile.age] = age
            }
            profile.resultedValues?.singleOrNull()?.let(::resultRowToProfile)
        }

    override suspend fun getUserProfile(profileid: Int):UserProfile?= dbQuery {
        Profile.select(Profile.profileid eq profileid).map(::resultRowToProfile).singleOrNull()
    }



    private fun resultRowToProfile(row: ResultRow) =
        UserProfile(
            profileid = row[Profile.profileid],
            userid = row[Profile.userid],
            email = row[Profile.email],
            age = row[Profile.age]
        )

}