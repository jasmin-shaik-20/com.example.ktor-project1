package com.example.repository
import UserProfileDao
import com.example.database.table.UserProfiles
import com.example.database.table.Users
import com.example.entities.UserEntity
import com.example.entities.UserProfileEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ProfileRepositoryImpl(id: EntityID<UUID>) : UUIDEntity(id), UserProfileDao {
    companion object : UUIDEntityClass<ProfileRepositoryImpl>(UserProfiles)

    override fun createUserProfile(userId: UUID, email: String, age: Int): UserProfileEntity {
        return transaction {
            val newProfile = UserProfileEntity.new {
                this.userId =  UserEntity[EntityID(userId, Users)]
                this.email = email
                this.age = age
            }
            newProfile
        }
    }


    override fun getUserProfileById(profileId: UUID): UserProfileEntity? {
        return transaction {
            UserProfileEntity.findById(profileId)
        }
    }

    override fun getUserProfileByUserId(userId: UUID):UserProfileEntity? {
        return transaction {
            UserProfileEntity.findById(userId)
        }
    }

    override fun getAllUserProfiles(): List<UserProfileEntity> {
        return transaction {
            UserProfileEntity.all().toList()
        }
    }

    override fun updateUserProfile(profileId: UUID, newEmail: String, newAge: Int): Boolean {
        return transaction {
            val userProfile = UserProfileEntity.findById(profileId)
            if (userProfile != null) {
                userProfile.email = newEmail
                userProfile.age = newAge
                true
            } else {
                false
            }
        }
    }

    override fun deleteUserProfile(profileId: UUID): Boolean {
        return transaction {
            val userProfile = UserProfileEntity.findById(profileId)
            if (userProfile != null) {
                userProfile.delete()
                true
            } else {
                false
            }
        }
    }
}
