package example.com.plugins.database


import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement

@Serializable
data class User(val id: Int = 0, val name: String)
class UsersSchema(private val connection: Connection) {

    companion object {
        private const val INSERT_USER = "INSERT INTO users (name) VALUES (?)"
        private const val GET_ALL_USERS = "SELECT * FROM users"
    }

    fun create(user: User) {
        val query = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)
        query.setString(1, user.name)
        query.executeUpdate()

    }

    fun getAllUsers(): List<User> {
        val statement = connection.prepareStatement(GET_ALL_USERS)
        val query = statement.executeQuery()
        val result = mutableListOf<User>()
        while(query.next()){
            result.add(User(query.getInt(1), query.getString(2)))
        }
        return result
    }
}