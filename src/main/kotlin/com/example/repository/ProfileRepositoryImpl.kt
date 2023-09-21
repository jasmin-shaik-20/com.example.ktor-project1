package com.example.repository
import UserProfileDao
import com.example.database.table.UserProfiles
import com.example.database.table.UserProfiles.userId
import com.example.database.table.Users
import com.example.entities.UserEntity
import com.example.entities.UserProfileEntity
import com.example.exceptions.UserNotFoundException
import com.example.model.UserProfile
import com.example.model.UserProfileInput
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToProfile
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProfileRepositoryImpl : UserProfileDao{
    override suspend fun createUserProfile(userProfileInput: UserProfileInput): UserProfile {
        return transaction {
            Users.select { Users.id eq UUID.fromString(userProfileInput.userId) }
                .singleOrNull() ?: throw UserNotFoundException()

            val insert = UserProfiles.insert {
                it[userId] = UUID.fromString(userProfileInput.userId)
                it[email] = userProfileInput.email
                it[age] = userProfileInput.age
            }

            val profileId = UUID.fromString(insert[UserProfiles.id].toString())

            UserProfile(profileId.toString(), userProfileInput.userId, userProfileInput.email, userProfileInput.age)
        }
    }


    override suspend fun getAllUserProfiles(): List<UserProfile> = dbQuery {
        UserProfiles.selectAll().map(::resultRowToProfile)
    }


    override suspend fun getUserProfileById(profileId: UUID):UserProfile?= dbQuery {
        UserProfiles.select(UserProfiles.id eq profileId).map(::resultRowToProfile).singleOrNull()
    }

    override suspend fun getUserProfileByUserId(userId: UUID):UserProfile? = dbQuery {
        UserProfiles.select(UserProfiles.userId eq userId).map(::resultRowToProfile).singleOrNull()
    }

    override suspend fun deleteUserProfile(profileId: UUID): Boolean = dbQuery{
        val delProfile=UserProfiles.deleteWhere { UserProfiles.id eq profileId }
        delProfile>0
    }

    override suspend fun updateUserProfile(profileId: UUID, newEmail: String, newAge: Int): Boolean = dbQuery{
        val editProfile=UserProfiles.update({UserProfiles.id eq profileId}){
            it[email]=newEmail
            it[age]=age
        }
        editProfile>0
    }


}
