import com.example.entities.UserProfileEntity
import com.example.model.UserProfile
import com.example.model.UserProfileInput
import java.util.UUID

interface UserProfileDao {
    suspend fun createUserProfile(userProfileInput: UserProfileInput): UserProfile?
    suspend fun getUserProfileById(profileId: UUID): UserProfile?

    suspend fun getUserProfileByUserId(userId: UUID):UserProfile?
    suspend fun getAllUserProfiles(): List<UserProfile>
    suspend fun updateUserProfile(profileId: UUID, newEmail: String, newAge: Int): Boolean
    suspend fun deleteUserProfile(profileId: UUID): Boolean
}
