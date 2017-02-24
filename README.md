# Design Patterns In Kotlin

#### :warning: Check my newest project: [Introduction to Unit Testing with Kotlin](https://github.com/dbacinski/Android-Testing-With-Kotlin/blob/master/docs/Unit-Testing-Introduction.md)

Project maintained by [@dbacinski](http://twitter.com/dbacinski) (Dariusz Baciński)

Based on [Design-Patterns-In-Swift](https://github.com/ochococo/Design-Patterns-In-Swift) by [@nsmeme](http://twitter.com/nsmeme) (Oktawian Chojnacki)

## Table of Contents

* [Behavioral Patterns](#behavioral)
	* [Observer / Listener](#observer--listener)
	* [Strategy](#strategy)
	* [Command](#command)
	* [State](#state)
	* [Chain of Responsibility](#chain-of-responsibility)
	* [Visitor](#visitor)
* [Creational Patterns](#creational)
	* [Builder / Assembler](#builder--assembler)
	* [Factory Method](#factory-method)
	* [Singleton](#singleton)
	* [Abstract Factory](#abstract-factory)
* [Structural Patterns](#structural)
	* [Adapter](#adapter)
	* [Decorator](#decorator)
	* [Facade](#facade)
	* [Protection Proxy](#protection-proxy)

Behavioral
==========

>In software engineering, behavioral design patterns are design patterns that identify common communication patterns between objects and realize these patterns. By doing so, these patterns increase flexibility in carrying out this communication.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Behavioral_pattern)

[Observer / Listener](/src/main/kotlin/Listener.kt)
--------

The observer pattern is used to allow an object to publish changes to its state. 
Other objects subscribe to be immediately notified of any changes.

#### Example

```kotlin
interface TextChangedListener {
    fun onTextChanged(newText: String)
}

class PrintingTextChangedListener : TextChangedListener {
    override fun onTextChanged(newText: String) = println("Text is changed to: $newText")
}

class TextView {

    var listener: TextChangedListener? = null

    var text: String by Delegates.observable("") { prop, old, new ->
        listener?.onTextChanged(new)
    }
}
```

#### Usage

```kotlin
val textView = TextView()
textView.listener = PrintingTextChangedListener()
textView.text = "Lorem ipsum"
textView.text = "dolor sit amet"
```

#### Output

```
Text is changed to: Lorem ipsum
Text is changed to: dolor sit amet
```

[Strategy](/src/main/kotlin/Strategy.kt)
-----------

The strategy pattern is used to create an interchangeable family of algorithms from which the required process is chosen at run-time.

#### Example

```kotlin
class Printer(val stringFormatterStrategy: (String) -> String) {
    fun printString(string: String) = println(stringFormatterStrategy.invoke(string))
}

val lowerCaseFormatter: (String) -> String = { it.toLowerCase() }

val upperCaseFormatter = { it: String -> it.toUpperCase() }
```

#### Usage

```kotlin
val lowerCasePrinter = Printer(lowerCaseFormatter)
lowerCasePrinter.printString("LOREM ipsum DOLOR sit amet")

val upperCasePrinter = Printer(upperCaseFormatter)
upperCasePrinter.printString("LOREM ipsum DOLOR sit amet")

val prefixPrinter = Printer({ "Prefix: " + it })
prefixPrinter.printString("LOREM ipsum DOLOR sit amet")
```

#### Output

```
lorem ipsum dolor sit amet
LOREM IPSUM DOLOR SIT AMET
Prefix: LOREM ipsum DOLOR sit amet
```

[Command](/src/main/kotlin/Command.kt)
-------

The command pattern is used to express a request, including the call to be made and all of its required parameters, in a command object. The command may then be executed immediately or held for later use.

#### Example:

```kotlin
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
```

#### Usage

```kotlin
CommandProcessor()
    .addToQueue(OrderAddCommand(1L))
    .addToQueue(OrderAddCommand(2L))
    .addToQueue(OrderPayCommand(2L))
    .addToQueue(OrderPayCommand(1L))
    .processCommands()
```

#### Output

```
adding order with id: 1
adding order with id: 2
paying for order with id: 2
paying for order with id: 1
```

[State](/src/main/kotlin/State.kt)
------

The state pattern is used to alter the behaviour of an object as its internal state changes. 
The pattern allows the class for an object to apparently change at run-time.

#### Example

```kotlin
sealed class AuthorizationState {

    class Unauthorized : AuthorizationState()

    class Authorized(val userName: String) : AuthorizationState()
}

class AuthorizationPresenter {

    private var state: AuthorizationState = Unauthorized()

    fun loginUser(userLogin: String) {
        state = Authorized(userLogin)
    }

    fun logoutUser() {
        state = Unauthorized()
    }

    val isAuthorized: Boolean
        get() {
            when (state) {
                is Authorized -> return true
                else -> return false
            }
        }

    val userLogin: String
        get() {
            when (state) {
                is Authorized -> return (state as Authorized).userName
                is Unauthorized -> return "Unknown"
            }
        }

    override fun toString(): String {
        return "User '$userLogin' is logged in: $isAuthorized"
    }
}
```

#### Usage

```kotlin
val authorization = AuthorizationPresenter()
authorization.loginUser("admin")
println(authorization)
authorization.logoutUser()
println(authorization)
```

#### Output

```
User 'admin' is logged in: true
User 'Unknown' is logged in: false
```

[Chain of Responsibility](/src/main/kotlin/ChainOfResponsibility.kt)
-----------------------

The chain of responsibility pattern is used to process varied requests, each of which may be dealt with by a different handler.

#### Example

```kotlin
interface MessageChain {
    fun addLines(inputHeader: String): String
}

class AuthenticationHeader(val token: String?, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String {
        token ?: throw IllegalStateException("Token should be not null")
        return "$inputHeader Authorization: Bearer $token\n".let { next?.addLines(it) ?: it }
    }
}

class ContentTypeHeader(val contentType: String, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String 
            = "$inputHeader ContentType: $contentType\n".let { next?.addLines(it) ?: it }
}

class BodyPayload(val body: String, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String
            = "$inputHeader $body\n".let { next?.addLines(it) ?: it }
}
```

#### Usage

```kotlin
val authenticationHeader = AuthenticationHeader("123456")
val contentTypeHeader = ContentTypeHeader("json")
val messageBody = BodyPayload("{\"username\"=\"dbacinski\"}")

val messageChainWithAuthorization = messageChainWithAuthorization(authenticationHeader, contentTypeHeader, messageBody)
val messageWithAuthentication = messageChainWithAuthorization.addLines("Message with Authentication:\n")
println(messageWithAuthentication)
    
fun messageChainWithAuthorization(authenticationHeader: AuthenticationHeader, contentTypeHeader: ContentTypeHeader, messageBody: BodyPayload): MessageChain {
    authenticationHeader.next = contentTypeHeader
    contentTypeHeader.next = messageBody
    return authenticationHeader
}
```

#### Output

```
Message with Authentication:
Authorization: Bearer 123456
ContentType: json
{"username"="dbacinski"}
```

[Visitor](/src/main/kotlin/Visitor.kt)
-------

The visitor pattern is used to separate a relatively complex set of structured data classes from the functionality that may be performed upon the data that they hold.

#### Example

```kotlin
interface ReportVisitable {
    fun accept(visitor: ReportVisitor)
}

class FixedPriceContract(val costPerYear: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

class TimeAndMaterialsContract(val costPerHour: Long, val hours: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

class SupportContract(val costPerMonth: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

interface ReportVisitor {
    fun visit(contract: FixedPriceContract)
    fun visit(contract: TimeAndMaterialsContract)
    fun visit(contract: SupportContract)
}

class MonthlyCostReportVisitor(var monthlyCost: Long = 0) : ReportVisitor {
    override fun visit(contract: FixedPriceContract) {
        monthlyCost += contract.costPerYear / 12
    }

    override fun visit(contract: TimeAndMaterialsContract) {
        monthlyCost += contract.costPerHour * contract.hours
    }

    override fun visit(contract: SupportContract) {
        monthlyCost += contract.costPerMonth
    }
}
```

#### Usage

```kotlin
val projectAlpha = FixedPriceContract(costPerYear = 10000)
val projectBeta = SupportContract(costPerMonth = 500)
val projectGamma = TimeAndMaterialsContract(hours = 150, costPerHour = 10)
val projectKappa = TimeAndMaterialsContract(hours = 50, costPerHour = 50)

val projects = arrayOf(projectAlpha, projectBeta, projectGamma, projectKappa)

val monthlyCostReportVisitor = MonthlyCostReportVisitor()
projects.forEach { it.accept(monthlyCostReportVisitor) }
println("Monthly cost: ${monthlyCostReportVisitor.monthlyCost}")
```

#### Output

```
Monthly cost: 5333
```

Creational
==========

> In software engineering, creational design patterns are design patterns that deal with object creation mechanisms, trying to create objects in a manner suitable to the situation. The basic form of object creation could result in design problems or added complexity to the design. Creational design patterns solve this problem by somehow controlling this object creation.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Creational_pattern)


[Builder / Assembler](/src/main/kotlin/Builder.kt)
----------

The builder pattern is used to create complex objects with constituent parts that must be created in the same order or using a specific algorithm. 
An external class controls the construction algorithm.

#### Example

```kotlin
// Let's assume that Dialog class is provided by external library.
// We have only access to Dialog public interface which cannot be changed.

class Dialog() {

    fun showTitle() = println("showing title")

    fun setTitle(text: String) = println("setting title text $text")

    fun setTitleColor(color: String) = println("setting title color $color")

    fun showMessage() = println("showing message")

    fun setMessage(text: String) = println("setting message $text")

    fun setMessageColor(color: String) = println("setting message color $color")

    fun showImage(bitmapBytes: ByteArray) = println("showing image with size ${bitmapBytes.size}")

    fun show() = println("showing dialog $this")
}

//Builder:
class DialogBuilder() {
    constructor(init: DialogBuilder.() -> Unit) : this() {
        init()
    }

    private var titleHolder: TextView? = null
    private var messageHolder: TextView? = null
    private var imageHolder: File? = null

    fun title(init: TextView.() -> Unit) {
        titleHolder = TextView().apply { init() }
    }

    fun message(init: TextView.() -> Unit) {
        messageHolder = TextView().apply { init() }
    }

    fun image(init: () -> File) {
        imageHolder = init()
    }

    fun build(): Dialog {
        val dialog = Dialog()

        titleHolder?.apply {
            dialog.setTitle(text)
            dialog.setTitleColor(color)
            dialog.showTitle()
        }

        messageHolder?.apply {
            dialog.setMessage(text)
            dialog.setMessageColor(color)
            dialog.showMessage()
        }

        imageHolder?.apply {
            dialog.showImage(readBytes())
        }

        return dialog
    }

    class TextView {
        var text: String = ""
        var color: String = "#00000"
    }
}
```

#### Usage

```kotlin
//Function that creates dialog builder and builds Dialog
fun dialog(init: DialogBuilder.() -> Unit): Dialog {
    return DialogBuilder(init).build()
}

val dialog: Dialog = dialog {
	title {
    	text = "Dialog Title"
    }
    message {
        text = "Dialog Message"
        color = "#333333"
    }
    image {
        File.createTempFile("image", "jpg")
    }
}

dialog.show()
```

#### Output

```
setting title text Dialog Title
setting title color #00000
showing title
setting message Dialog Message
setting message color #333333
showing message
showing image with size 0
showing dialog Dialog@5f184fc6
```

[Factory Method](/src/main/kotlin/FactoryMethod.kt)
-----------------

The factory pattern is used to replace class constructors, abstracting the process of object generation so that the type of the object instantiated can be determined at run-time.

#### Example

```kotlin
interface Currency {
    val code: String
}

class Euro(override val code: String = "EUR") : Currency
class UnitedStatesDollar(override val code: String = "USD") : Currency

enum class Country {
    UnitedStates, Spain, UK, Greece
}

class CurrencyFactory {
    fun currencyForCountry(country: Country): Currency? {
        when (country) {
            Country.Spain, Country.Greece -> return Euro()
            Country.UnitedStates          -> return UnitedStatesDollar()
            else                          -> return null
        }
    }
}
```

#### Usage

```kotlin
val noCurrencyCode = "No Currency Code Available"

val greeceCode = CurrencyFactory().currencyForCountry(Country.Greece)?.code() ?: noCurrencyCode
println("Greece currency: $greeceCode")

val usCode = CurrencyFactory().currencyForCountry(Country.UnitedStates)?.code() ?: noCurrencyCode
println("US currency: $usCode")

val ukCode = CurrencyFactory().currencyForCountry(Country.UK)?.code() ?: noCurrencyCode
println("UK currency: $ukCode")
```

#### Output

```
Greece currency: EUR
US currency: USD
UK currency: No Currency Code Available
```

[Singleton](/src/main/kotlin/Singleton.kt)
------------

The singleton pattern ensures that only one object of a particular class is ever created.
All further references to objects of the singleton class refer to the same underlying instance.
There are very few applications, do not overuse this pattern!

#### Example:

```kotlin
object PrinterDriver {
    init {
        println("Initializing with object: $this")
    }

    fun print() = println("Printing with object: $this")
}
```

#### Usage

```kotlin
println("Start")
PrinterDriver.print()
PrinterDriver.print()
```

#### Output

```
Start
Initializing with object: PrinterDriver@6ff3c5b5
Printing with object: PrinterDriver@6ff3c5b5
Printing with object: PrinterDriver@6ff3c5b5
```

[Abstract Factory](/src/main/kotlin/AbstractFactory.kt)
-------------------

The abstract factory pattern is used to provide a client with a set of related or dependant objects. 
The "family" of objects created by the factory are determined at run-time.

#### Example

```kotlin
interface Plant

class OrangePlant : Plant

class ApplePlant : Plant

abstract class PlantFactory {
    abstract fun makePlant(): Plant

    companion object {
        inline fun <reified T : Plant> createFactory(): PlantFactory = when (T::class) {
            OrangePlant::class -> OrangeFactory()
            ApplePlant::class  -> AppleFactory()
            else               -> throw IllegalArgumentException()
        }
    }
}

class AppleFactory : PlantFactory() {
    override fun makePlant(): Plant = ApplePlant()
}

class OrangeFactory : PlantFactory() {
    override fun makePlant(): Plant = OrangePlant()
}
```

#### Usage

```kotlin
val plantFactory = PlantFactory.createFactory(OrangePlant::class)
val plant = plantFactory.makePlant()
println("Created plant: $plant")
```

#### Output

```kotlin
Created plant: OrangePlant@4f023edb
```

Structural
==========

>In software engineering, structural design patterns are design patterns that ease the design by identifying a simple way to realize relationships between entities.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Structural_pattern)

[Adapter](/src/main/kotlin/Adapter.kt)
----------

The adapter pattern is used to provide a link between two otherwise incompatible types by wrapping the "adaptee" with a class that supports the interface required by the client.

#### Example

```kotlin
interface Temperature {
    var temperature: Double
}

class CelsiusTemperature(override var temperature: Double) : Temperature

class FahrenheitTemperature(var celsiusTemperature: CelsiusTemperature) : Temperature {

    override var temperature: Double
        get() = convertCelsiusToFahrenheit(celsiusTemperature.temperature)
        set(temperatureInF) {
            celsiusTemperature.temperature = convertFahrenheitToCelsius(temperatureInF)
        }

    private fun convertFahrenheitToCelsius(f: Double): Double = (f - 32) * 5 / 9

    private fun convertCelsiusToFahrenheit(c: Double): Double = (c * 9 / 5) + 32
}

```

#### Usage

```kotlin
val celsiusTemperature = CelsiusTemperature(0.0)
val fahrenheitTemperature = FahrenheitTemperature(celsiusTemperature)

celsiusTemperature.temperature = 36.6
println("${celsiusTemperature.temperature} C -> ${fahrenheitTemperature.temperature} F")

fahrenheitTemperature.temperature = 100.0
println("${fahrenheitTemperature.temperature} F -> ${celsiusTemperature.temperature} C")
```

#### Output

```
36.6 C -> 97.88000000000001 F
100.0 F -> 37.77777777777778 C
```

[Decorator](/src/main/kotlin/Decorator.kt)
------------

The decorator pattern is used to extend or alter the functionality of objects at run-time by wrapping them in an object of a decorator class. 
This provides a flexible alternative to using inheritance to modify behaviour.

#### Example

```kotlin
interface CoffeeMachine {
    fun makeSmallCoffee()
    fun makeLargeCoffee()
}

class NormalCoffeeMachine : CoffeeMachine {
    override fun makeSmallCoffee() = println("Normal: Making small coffee")

    override fun makeLargeCoffee() = println("Normal: Making large coffee")
}

//Decorator:
class EnhancedCoffeeMachine(val coffeeMachine: CoffeeMachine) : CoffeeMachine by coffeeMachine {

    fun makeCoffeeWithMilk() {
        println("Enhanced: Making coffee with milk")
        coffeeMachine.makeSmallCoffee()
        println("Enhanced: Adding milk")
    }

    fun makeDoubleLargeCoffee() {
        println("Enhanced: Making double large coffee")
        coffeeMachine.makeLargeCoffee()
        coffeeMachine.makeLargeCoffee()
    }
}
```

#### Usage

```kotlin
val normalMachine = NormalCoffeeMachine()
val enhancedMachine = EnhancedCoffeeMachine(normalMachine)

enhancedMachine.makeCoffeeWithMilk()

enhancedMachine.makeDoubleLargeCoffee()
```

#### Output

```
Enhanced: Making coffee with milk
Normal: Making small coffee
Enhanced: Adding milk

Enhanced: Making double large coffee
Normal: Making large coffee
Normal: Making large coffee
```

[Facade](/src/main/kotlin/Facade.kt)
---------

The facade pattern is used to define a simplified interface to a more complex subsystem.

#### Example

```kotlin
class ComplexSystemStore(val filePath: String) {

    init {
        println("Reading data from file: $filePath")
    }

    val store = HashMap<String, String>()

    fun store(key: String, payload: String) {
        store.put(key, payload)
    }

    fun read(key: String): String = store[key] ?: ""

    fun commit() = println("Storing cached data: $store to file: $filePath")
}

data class User(val login: String)

//Facade:
class UserRepository {
    val systemPreferences = ComplexSystemStore("/data/default.prefs")

    fun save(user: User) {
        systemPreferences.store("USER_KEY", user.login)
        systemPreferences.commit()
    }

    fun findFirst(): User = User(systemPreferences.read("USER_KEY"))
}
```

#### Usage

```kotlin
val userRepository = UserRepository()
val user = User("dbacinski")
userRepository.save(user)
val resultUser = userRepository.findFirst()
println("Found stored user: $resultUser")
```

#### Ouput

```
Reading data from file: /data/default.prefs
Storing cached data: {USER_KEY=dbacinski} to file: /data/default.prefs
Found stored user: User(login=dbacinski)
```

[Protection Proxy](/src/main/kotlin/ProtectionProxy.kt)
------------------

The proxy pattern is used to provide a surrogate or placeholder object, which references an underlying object. 
Protection proxy is restricting access.

#### Example

```kotlin
interface File {
    fun read(name: String)
}

class NormalFile : File {
    override fun read(name: String) = println("Reading file: $name")
}

//Proxy:
class SecuredFile : File {
    val normalFile = NormalFile()
    var password: String = ""

    override fun read(name: String) {
        if (password == "secret") {
            println("Password is correct: $password")
            normalFile.read(name)
        } else {
            println("Incorrect password. Access denied!")
        }
    }
}
```

#### Usage

```kotlin
val securedFile = SecuredFile()
securedFile.read("readme.md")

securedFile.password = "secret"
securedFile.read("readme.md")
```

#### Ouput

```
Incorrect password. Access denied!
Password is correct: secret
Reading file: readme.md
```

Info
====

Descriptions from: [Gang of Four Design Patterns Reference Sheet](http://www.blackwasp.co.uk/GangOfFour.aspx)
