import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

data class Memento(val state: String)

class Originator(var state: String) {

    fun createMemento(): Memento {
        return Memento(state)
    }

    fun restore(memento: Memento) {
        state = memento.state
    }
}

class CareTaker {
    private val mementoList = ArrayList<Memento>()

    fun saveState(state: Memento) {
        mementoList.add(state)
    }

    fun restore(index: Int): Memento {
        return mementoList[index]
    }
}

class MementoTest {

    @Test
    fun Memento() {
        val originator = Originator("initial state")
        val careTaker = CareTaker()
        careTaker.saveState(originator.createMemento())

        originator.state = "State #1"
        originator.state = "State #2"
        careTaker.saveState(originator.createMemento())

        originator.state = "State #3"
        println("Current State: " + originator.state)
        assertThat(originator.state).isEqualTo("State #3")

        originator.restore(careTaker.restore(1))
        println("Second saved state: " + originator.state)
        assertThat(originator.state).isEqualTo("State #2")

        originator.restore(careTaker.restore(0))
        println("First saved state: " + originator.state)
        assertThat(originator.state).isEqualTo("initial state")
    }
}