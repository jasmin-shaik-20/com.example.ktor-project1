package com.example.entities

import com.example.database.table.UserProfiles
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserProfileEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserProfileEntity>(UserProfiles)
    var userId by UserEntity referencedOn UserProfiles.userId
    var email by UserProfiles.email
    var age by UserProfiles.age
}