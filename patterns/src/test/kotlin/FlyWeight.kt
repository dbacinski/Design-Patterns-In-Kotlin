abstract class RaceCar {
    internal var name: String? = null
    internal var speed: Int = 0
    internal var horsePower: Int = 0

    internal abstract fun moveCar(currentX: Int, currentY: Int, newX: Int, newY: Int)

}

class FlyWeightMidgetCar : RaceCar() {
    init {
        num++
    }

    override fun moveCar(currentX: Int, currentY: Int, newX: Int, newY: Int) {
        println("New location of " + this.name + " is X" + newX + " - Y" + newY)
    }

    companion object {
        var num: Int = 0
    }
}

// Factory:
object CarFactory {
    private val flyweights = HashMap<String, RaceCar>()
    fun getRaceCar(key: String): RaceCar? {
        if (flyweights.containsKey(key)) {
            return flyweights[key]
        }
        val raceCar: RaceCar
        when (key) {
            "Midget" -> {
                raceCar = FlyWeightMidgetCar()
                raceCar.name = "Midget Car"
                raceCar.speed = 140
                raceCar.horsePower = 400
            }
            else ->
                throw IllegalArgumentException("Unsupported car type.")
        }
        flyweights[key] = raceCar
        return raceCar
    }
}

class RaceCarClient(name: String) {
    private val raceCar: RaceCar = CarFactory.getRaceCar(name)!!
    private var currentX = 0
    private var currentY = 0
    fun moveCar(newX: Int, newY: Int) {
        raceCar.moveCar(currentX, currentY, newX, newY)
        currentX = newX
        currentY = newY
    }
}


fun main(){
    val raceCars = arrayOf(

            RaceCarClient("Midget"),
            RaceCarClient("Midget"),
            RaceCarClient("Midget"))
    raceCars[0].moveCar(29, 3112)
    raceCars[1].moveCar(39, 2002)
    raceCars[2].moveCar(49, 1985)
    println("Midget Car Instances: " + FlyWeightMidgetCar.num)
}