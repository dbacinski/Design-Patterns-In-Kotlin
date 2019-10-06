import org.junit.jupiter.api.Test
import java.util.*

interface OrderCommand {
    fun execute()
}

class OrderAddCommand(private val id: Long) : OrderCommand {
    override fun execute() = println("Adding order with id: $id")
}

class OrderPayCommand(private val id: Long) : OrderCommand {
    override fun execute() = println("Paying for order with id: $id")
}

class CommandProcessor {

    private val queue = ArrayList<OrderCommand>()

    fun addToQueue(orderCommand: OrderCommand): CommandProcessor =
        apply {
            queue.add(orderCommand)
        }

    fun processCommands(): CommandProcessor =
        apply {
            queue.forEach { it.execute() }
            queue.clear()
        }
}

class CommandTest {

    @Test
    fun Command() {
        CommandProcessor()
            .addToQueue(OrderAddCommand(1L))
            .addToQueue(OrderAddCommand(2L))
            .addToQueue(OrderPayCommand(2L))
            .addToQueue(OrderPayCommand(1L))
            .processCommands()
    }
}
