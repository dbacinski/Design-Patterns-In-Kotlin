import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

data class Personal(
        val name: String,
        val age: Int,
        val country: String,
        val gender: Gender
)

enum class Gender {
    FEMALE, MALE, OTHER
}

fun Any.getObjectSignature(): String = "${this.javaClass.name}@${Integer.toHexString(System.identityHashCode(this))}"

class PrototypeTest {

    @Test
    fun Prototype() {
        val personal = Personal(
                name = "Emanuel",
                age = 20,
                country = "Brazil",
                gender = Gender.MALE
        )
        val personalClone = personal.copy()

        println("personal printing with values $personal and with object ${personal.getObjectSignature()}")
        println("personalClone printing with values $personalClone and with object ${personalClone.getObjectSignature()}")

        assertThat(personal).isNotSameAs(personalClone)
        assertThat(personal).isEqualTo(personalClone)

    }
}