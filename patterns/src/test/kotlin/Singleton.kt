import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

object PrinterDriver {
    init {
        println("Initializing with object: $this")
    }

    fun print(): PrinterDriver =
        apply { println("Printing with object: $this") }
}

class SingletonTest {

    @Test
    fun Singleton() {
        println("Start")
        val printerFirst = PrinterDriver.print()
        val printerSecond = PrinterDriver.print()

        assertThat(printerFirst).isSameAs(PrinterDriver)
        assertThat(printerSecond).isSameAs(PrinterDriver)
    }
}
