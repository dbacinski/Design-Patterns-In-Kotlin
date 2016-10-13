import java.util.*

interface OrderCommand {
    fun execute()
}

class OrderAddCommand(val id: Long) : OrderCommand {
    override fun execute() = println("adding order with id: $id")
}

class OrderPayCommand(val id: Long) : OrderCommand {
    override fun execute() = println("paying for order with id: $id")
}

class CommandProcessor {

    private val queue = ArrayList<OrderCommand>()

    fun addToQueue(orderCommand: OrderCommand): CommandProcessor
            = apply { queue.add(orderCommand) }

    fun processCommands(): CommandProcessor = apply {
        queue.forEach { it.execute() }
        queue.clear()
    }
}

fun main(args: Array<String>) {
    CommandProcessor()
            .addToQueue(OrderAddCommand(1L))
            .addToQueue(OrderAddCommand(2L))
            .addToQueue(OrderPayCommand(2L))
            .addToQueue(OrderPayCommand(1L))
            .processCommands()
}
