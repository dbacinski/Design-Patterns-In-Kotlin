import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

sealed class AuthorizationState

object Unauthorized : AuthorizationState()

class Authorized(val userName: String) : AuthorizationState()

class AuthorizationPresenter {

    private var state: AuthorizationState = Unauthorized

    fun loginUser(userLogin: String) {
        state = Authorized(userLogin)
    }

    fun logoutUser() {
        state = Unauthorized
    }

    val isAuthorized: Boolean
        get() = when (state) {
            is Authorized -> true
            is Unauthorized -> false
        }

    val userLogin: String
        get() {
            val state = this.state //val enables smart casting of state
            return when (state) {
                is Authorized -> state.userName
                is Unauthorized -> "Unknown"
            }
        }

    override fun toString() = "User '$userLogin' is logged in: $isAuthorized"
}

class StateTest {

    @Test
    fun `State`() {
        val authorizationPresenter = AuthorizationPresenter()

        authorizationPresenter.loginUser("admin")
        println(authorizationPresenter)
        assertThat(authorizationPresenter.isAuthorized).isEqualTo(true)
        assertThat(authorizationPresenter.userLogin).isEqualTo("admin")

        authorizationPresenter.logoutUser()
        println(authorizationPresenter)
        assertThat(authorizationPresenter.isAuthorized).isEqualTo(false)
        assertThat(authorizationPresenter.userLogin).isEqualTo("Unknown")
    }
}

