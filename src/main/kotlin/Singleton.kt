class PrinterDriver() {
    fun print() = println("Printing with object: $this")

    companion object {
        val instance: PrinterDriver by lazy {
            PrinterDriver().apply { println("Initializing with object: $this") }
        }
    }
}

fun main(args: Array<String>) {
    println("Start")
    PrinterDriver.instance.print()
    PrinterDriver.instance.print()
}