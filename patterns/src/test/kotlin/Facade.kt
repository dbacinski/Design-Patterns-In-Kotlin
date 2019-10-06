import org.junit.jupiter.api.Test

class ComplexSystemStore(private val filePath: String) {

    private val cache: HashMap<String, String>

    init {
        println("Reading data from file: $filePath")
        cache = HashMap()
        //read properties from file and put to cache
    }

    fun store(key: String, payload: String) {
        cache[key] = payload
    }

    fun read(key: String): String = cache[key] ?: ""

    fun commit() = println("Storing cached data: $cache to file: $filePath")
}

data class User(val login: String)

//Facade:
class UserRepository {

    private val systemPreferences = ComplexSystemStore("/data/default.prefs")

    fun save(user: User) {
        systemPreferences.store("USER_KEY", user.login)
        systemPreferences.commit()
    }

    fun findFirst(): User = User(systemPreferences.read("USER_KEY"))
}

class FacadeTest {

    @Test
    fun Facade() {
        val userRepository = UserRepository()
        val user = User("dbacinski")
        userRepository.save(user)
        val resultUser = userRepository.findFirst()
        println("Found stored user: $resultUser")
    }
}