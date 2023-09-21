import com.example.dao.CourseDao
import com.example.database.table.Courses
import com.example.database.table.StudentCourses
import com.example.database.table.Students
import com.example.entities.CourseEntity
import com.example.entities.StudentEntity
import com.example.exceptions.StudentNotFoundException
import com.example.model.Course
import com.example.model.CourseInput
import com.example.plugins.dbQuery
import com.example.utils.helperFunctions.resultRowToCourse
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CourseRepositoryImpl:CourseDao {

    override suspend fun createCourse(courseInput: CourseInput): Course {
        return transaction {
            Students.select { Students.id eq UUID.fromString(courseInput.studentId)}.
            singleOrNull()?:throw StudentNotFoundException()
            val insert=Courses.insert {
                it[studentId]=UUID.fromString(courseInput.studentId)
                it[name]=courseInput.name
            }
            StudentCourses.insert {
                it[studentId]=UUID.fromString(courseInput.studentId)
                it[courseId]=UUID.fromString(insert[Courses.id].toString())
            }
            val courseId=UUID.fromString(insert[Courses.id].toString())
            Course(courseId.toString(),courseInput.studentId,courseInput.name)
        }
    }

    override suspend fun getAllCourses(): List<Course> = dbQuery {
        Courses.selectAll().map(::resultRowToCourse)
    }

    override suspend fun deleteCourse(id: UUID): Boolean = dbQuery{
        val delCourse=Courses.deleteWhere { Courses.id eq id }
        delCourse>0
    }

    override suspend fun updateCourse(id: UUID, newName: String): Boolean = dbQuery {
       val editCourse=Courses.update({Courses.id eq id}){
           it[name]=newName
       }
        editCourse>0
    }

    override suspend fun getCourseById(id: UUID): Course? = dbQuery {
        Courses.select(Courses.id eq id ).map(::resultRowToCourse).singleOrNull()
    }
}
