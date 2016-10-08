import kotlin.reflect.KClass

//Based on: http://stackoverflow.com/a/13030163/361832

interface Plant

class OrangePlant : Plant

class ApplePlant : Plant

abstract class PlantFactory {
    abstract fun makePlant(): Plant

    companion object {
        fun createFactory(plant: KClass<out Plant>): PlantFactory {
            when (plant) {
                OrangePlant::class -> return OrangeFactory()
                ApplePlant::class -> return AppleFactory()
                else -> throw IllegalArgumentException()
            }
        }
    }
}

class AppleFactory : PlantFactory() {
    override fun makePlant(): Plant {
        return ApplePlant()
    }
}

class OrangeFactory : PlantFactory() {
    override fun makePlant(): Plant {
        return OrangePlant()
    }
}

fun main(args: Array<String>) {
    val plantFactory = PlantFactory.createFactory(OrangePlant::class)
    val plant = plantFactory.makePlant()
    println("Created plant: $plant")
}