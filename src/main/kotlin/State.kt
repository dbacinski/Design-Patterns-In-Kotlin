interface AuthorizationState {
    fun isAuthorized(): Boolean
    fun userId(): String?
}

class UnauthorizedState() : AuthorizationState {
    override fun isAuthorized(): Boolean = false

    override fun userId(): String? = null
}

class AuthorizedState(val userName: String?) : AuthorizationState {
    override fun isAuthorized(): Boolean = true

    override fun userId(): String? = userName
}

class Authorization {
    private var state: AuthorizationState = UnauthorizedState()

    var isAuthorized: Boolean = false
        get() = state.isAuthorized()

    var userLogin: String? = null
        get() = state.userId()

    fun loginUser(userLogin: String) {
        state = AuthorizedState(userLogin)
    }

    fun logoutUser(userId: String) {
        state = UnauthorizedState()
    }

    override fun toString(): String {
        return "User '$userLogin' is logged in: $isAuthorized"
    }
}

fun main(args: Array<String>) {
    val authorization = Authorization()
    authorization.loginUser("admin")
    println(authorization.toString())
    authorization.logoutUser("admin")
    println(authorization.toString())
}