import org.junit.jupiter.api.Test

class Printer(private val stringFormatterStrategy: (String) -> String) {

    fun printString(string: String) {
        println(stringFormatterStrategy(string))
    }
}

class StrategyTest {

    @Test
    fun `Strategy`() {
        val lowerCaseFormatter: (String) -> String = { it.toLowerCase() }
        val upperCaseFormatter = { it: String -> it.toUpperCase() }

        val inputString = "LOREM ipsum DOLOR sit amet"

        val lowerCasePrinter = Printer(lowerCaseFormatter)
        lowerCasePrinter.printString(inputString)

        val upperCasePrinter = Printer(upperCaseFormatter)
        upperCasePrinter.printString(inputString)

        val prefixPrinter = Printer { "Prefix: $it" }
        prefixPrinter.printString(inputString)
    }
}

