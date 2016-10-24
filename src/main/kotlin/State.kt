import AuthorizationState.Authorized
import AuthorizationState.Unauthorized

sealed class AuthorizationState {

    class Unauthorized : AuthorizationState()

    class Authorized(val userName: String) : AuthorizationState()
}

class AuthorizationPresenter {

    private var state: AuthorizationState = Unauthorized()

    fun loginUser(userLogin: String) {
        state = Authorized(userLogin)
    }

    fun logoutUser() {
        state = Unauthorized()
    }

    val isAuthorized: Boolean
        get() {
            when (state) {
                is Authorized -> return true
                else -> return false
            }
        }

    val userLogin: String
        get() {
            when (state) {
                is Authorized -> return (state as Authorized).userName
                is Unauthorized -> return "Unknown"
            }
        }

    override fun toString(): String {
        return "User '$userLogin' is logged in: $isAuthorized"
    }
}

fun main(args: Array<String>) {
    val authorization = AuthorizationPresenter()
    authorization.loginUser("admin")
    println(authorization)
    authorization.logoutUser()
    println(authorization)
}