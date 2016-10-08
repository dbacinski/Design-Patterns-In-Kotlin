class PrinterDriver() {
    fun print() = println("Printing with object: $this")
}

object PrinterDriverSingleton {
    val instance: PrinterDriver

    init {
        instance = PrinterDriver()
        println("Initializing with object: $instance")
    }
}

fun main(args: Array<String>) {
    println("Start");
    PrinterDriverSingleton.instance.print()
    PrinterDriverSingleton.instance.print()
}