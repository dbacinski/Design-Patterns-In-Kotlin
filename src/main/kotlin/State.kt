interface State {
    fun isAuthorized(): Boolean
    fun userId(): String?
}

class UnauthorizedState() : State {
    override fun isAuthorized(): Boolean {
        return false
    }

    override fun userId(): String? {
        return null
    }
}

class AuthorizedState(val userName: String?) : State {
    override fun isAuthorized(): Boolean {
        return true
    }

    override fun userId(): String? {
        return userName
    }
}

class Authorization {
    private var state: State = UnauthorizedState()

    var isAuthorized: Boolean = false
        get() {
            return state.isAuthorized()
        }

    var userLogin: String? = null
        get() {
            return state.userId()
        }

    fun loginUser(userLogin: String) {
        state = AuthorizedState(userLogin)
    }

    fun logoutUser(userId: String) {
        state = UnauthorizedState()
    }

}

fun main(args: Array<String>) {
    val authorization = Authorization()

    authorization.loginUser("admin")
    printUserAuthorization(authorization)
    authorization.logoutUser("admin")
    printUserAuthorization(authorization)

}

private fun printUserAuthorization(authorization: Authorization) {
    println("User '${authorization.userLogin}' is logged in: ${authorization.isAuthorized}")
}
