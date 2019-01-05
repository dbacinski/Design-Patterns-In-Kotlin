open class Equipment(
    open val price: Int,
    val name: String
)

open class Composite(name: String) : Equipment(0, name) {

    private val equipments = ArrayList<Equipment>()

    override val price: Int
        get() = equipments.map { it -> it.price }.sum()


    fun add(equipment: Equipment) =
        apply { equipments.add(equipment) }
}

class PersonalComputer : Composite("PC")
class Processor : Equipment(1070, "Processor")
class HardDrive : Equipment(250, "Hard Drive")
class Memory : Equipment(280, "Memory")


fun main(args: Array<String>) {
    val pc = PersonalComputer()
        .add(Processor())
        .add(HardDrive())
        .add(Memory())

    println(pc.price)
}