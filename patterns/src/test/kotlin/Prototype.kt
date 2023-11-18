import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

open class Cat : Cloneable {
    var sound = ""
    var species = ""

    init {
        sound = "Meow"
        species = "Ordinary"
    }

    public override fun clone(): Cat = Cat()

    fun powerUp() {
        sound = "MEOW!!!"
        species = "Super cat"
    }
}

fun makeSuperCat(cat: Cat): Cat = cat.apply { powerUp() }

class PrototypeTest {

    @Test
    fun Prototype() {
        val ordinaryCat = Cat()
        val copiedCat = ordinaryCat.clone()

        assertEquals(ordinaryCat.sound, copiedCat.sound)

        val superCat = makeSuperCat(copiedCat)

        assertEquals(copiedCat.sound, superCat.sound)
    }
}
