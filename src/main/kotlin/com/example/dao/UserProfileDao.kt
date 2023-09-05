import com.example.entities.UserProfileEntity
import java.util.UUID

interface UserProfileDao {
    fun createUserProfile(userId: UUID, email: String, age: Int): UserProfileEntity
    fun getUserProfileById(profileId: UUID): UserProfileEntity?

    fun getUserProfileByUserId(userId: UUID):UserProfileEntity?
    fun getAllUserProfiles(): List<UserProfileEntity>
    fun updateUserProfile(profileId: UUID, newEmail: String, newAge: Int): Boolean
    fun deleteUserProfile(profileId: UUID): Boolean
}
