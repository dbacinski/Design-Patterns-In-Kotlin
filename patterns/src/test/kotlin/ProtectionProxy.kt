import org.junit.jupiter.api.Test

interface File {
    fun read(name: String)
}

class NormalFile : File {
    override fun read(name: String) = println("Reading file: $name")
}

//Proxy:
class SecuredFile(private val normalFile: File) : File {
    var password: String = ""

    override fun read(name: String) {
        if (password == "secret") {
            println("Password is correct: $password")
            normalFile.read(name)
        } else {
            println("Incorrect password. Access denied!")
        }
    }
}

class ProtectionProxyTest {
    @Test
    fun `Protection Proxy`() {
        val securedFile = SecuredFile(NormalFile())

        with(securedFile) {
            read("readme.md")
            password = "secret"
            read("readme.md")
        }
    }
}