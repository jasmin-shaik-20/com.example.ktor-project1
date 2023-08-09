import com.example.dao.User

interface UsersInterface {
    suspend fun createUser(id: Int, name: String): User?
    suspend fun selectUser(id:Int):User?
}