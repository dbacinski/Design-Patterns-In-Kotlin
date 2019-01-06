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

fun main(args: Array<String>) {
    val originator = Originator("initial state")
    val careTaker = CareTaker()
    careTaker.saveState(originator.createMemento())

    originator.state = "State #1"
    originator.state = "State #2"
    careTaker.saveState(originator.createMemento())

    originator.state = "State #3"
    careTaker.saveState(originator.createMemento())

    originator.state = "State #4"
    println("Current State: " + originator.state)

    originator.restore(careTaker.restore(0))
    println("First saved State: " + originator.state)
    originator.restore(careTaker.restore(1))
    println("Second saved State: " + originator.state)
    originator.restore(careTaker.restore(2))
    println("Second saved State: " + originator.state)
}