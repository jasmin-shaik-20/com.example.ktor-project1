import com.example.dao.CourseDao
import com.example.database.table.Courses
import com.example.database.table.StudentCourses
import com.example.database.table.Students
import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CourseRepositoryImpl(id: EntityID<UUID>) : UUIDEntity(id), CourseDao {
    companion object : UUIDEntityClass<CourseRepositoryImpl>(Courses)

    override suspend fun createCourse(studentId: UUID, name: String): CourseEntity {
        return transaction {
            val newCourse = CourseEntity.new {
                this.studentId = StudentEntity[EntityID(studentId, Students)]
                this.name = name
            }
            StudentCourses.batchInsert(listOf(StudentCourses.studentId, StudentCourses.courseId)) {
                this[StudentCourses.studentId] = EntityID(studentId, Students)
                this[StudentCourses.courseId] = newCourse.id
            }
            newCourse
        }
    }

    override suspend fun getAllCourses(): List<CourseEntity> {
        return transaction {
            CourseEntity.all().toList()
        }
    }

    override suspend fun deleteCourse(id: UUID): Boolean {
        return transaction {
            val course = CourseEntity.findById(id)
            if (course != null) {
                course.delete()
                true
            } else {
                false
            }
        }
    }

    override suspend fun updateCourse(id: UUID, newName: String): Boolean {
        return transaction {
            val course = CourseEntity.findById(id)
            if (course != null) {
                course.name = newName
                true
            } else {
                false
            }
        }
    }

    override suspend fun getCourseById(id: UUID): CourseEntity? {
        return transaction {
            CourseEntity.findById(id)
        }
    }
}
