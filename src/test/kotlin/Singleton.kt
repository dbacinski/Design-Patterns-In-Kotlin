object PrinterDriver {
    init {
        println("Initializing with object: $this")
    }

    fun print() = println("Printing with object: $this")
}

fun main(args: Array<String>) {
    println("Start")
    PrinterDriver.print()
    PrinterDriver.print()
}
