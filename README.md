# Design Patterns In Kotlin

#### :warning: New article about testing: [Unit Testing with Mockito 2](https://github.com/dbacinski/Android-Testing-With-Kotlin/blob/master/docs/Unit-Testing-Mockito.md)

Project maintained by [@dbacinski](http://twitter.com/dbacinski) (Dariusz Baciński)

Inspired by [Design-Patterns-In-Swift](https://github.com/ochococo/Design-Patterns-In-Swift) by [@nsmeme](http://twitter.com/nsmeme) (Oktawian Chojnacki)

## Table of Contents

* [Behavioral Patterns](#behavioral)
	* [Observer / Listener](#observer--listener)
	* [Strategy](#strategy)
	* [Command](#command)
	* [State](#state)
	* [Chain of Responsibility](#chain-of-responsibility)
	* [Visitor](#visitor)
	* [Mediator](#mediator)
	* [Memento](#memento)
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
	* [Composite](#composite)

Behavioral
==========

>In software engineering, behavioral design patterns are design patterns that identify common communication patterns between objects and realize these patterns. By doing so, these patterns increase flexibility in carrying out this communication.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Behavioral_pattern)

[Observer / Listener](/patterns/src/test/kotlin/Listener.kt)
--------

The observer pattern is used to allow an object to publish changes to its state.
Other objects subscribe to be immediately notified of any changes.

#### Example

```kotlin
interface TextChangedListener {

    fun onTextChanged(oldText: String, newText: String)
}

class PrintingTextChangedListener : TextChangedListener {
    
    private var text = ""
    
    override fun onTextChanged(oldText: String, newText: String) {
        text = "Text is changed: $oldText -> $newText"
    }
}

class TextView {

    val listeners = mutableListOf<TextChangedListener>()

    var text: String by Delegates.observable("<empty>") { _, old, new ->
        listeners.forEach { it.onTextChanged(old, new) }
    }
}
```

#### Usage

```kotlin
val textView = TextView().apply {
    listener = PrintingTextChangedListener()
}

with(textView) {
    text = "Lorem ipsum"
    text = "dolor sit amet"
}
```

#### Output

```
Text is changed <empty> -> Lorem ipsum
Text is changed Lorem ipsum -> dolor sit amet
```

[Strategy](/patterns/src/test/kotlin/Strategy.kt)
-----------

The strategy pattern is used to create an interchangeable family of algorithms from which the required process is chosen at run-time.

#### Example

```kotlin
class Printer(private val stringFormatterStrategy: (String) -> String) {

    fun printString(string: String) {
        println(stringFormatterStrategy(string))
    }
}

val lowerCaseFormatter: (String) -> String = { it.toLowerCase() }
val upperCaseFormatter = { it: String -> it.toUpperCase() }
```

#### Usage

```kotlin
val inputString = "LOREM ipsum DOLOR sit amet"

val lowerCasePrinter = Printer(lowerCaseFormatter)
lowerCasePrinter.printString(inputString)

val upperCasePrinter = Printer(upperCaseFormatter)
upperCasePrinter.printString(inputString)

val prefixPrinter = Printer { "Prefix: $it" }
prefixPrinter.printString(inputString)
```

#### Output

```
lorem ipsum dolor sit amet
LOREM IPSUM DOLOR SIT AMET
Prefix: LOREM ipsum DOLOR sit amet
```

[Command](/patterns/src/test/kotlin/Command.kt)
-------

The command pattern is used to express a request, including the call to be made and all of its required parameters, in a command object. The command may then be executed immediately or held for later use.

#### Example:

```kotlin
interface OrderCommand {
    fun execute()
}

class OrderAddCommand(val id: Long) : OrderCommand {
    override fun execute() = println("Adding order with id: $id")
}

class OrderPayCommand(val id: Long) : OrderCommand {
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
Adding order with id: 1
Adding order with id: 2
Paying for order with id: 2
Paying for order with id: 1
```

[State](/patterns/src/test/kotlin/State.kt)
------

The state pattern is used to alter the behaviour of an object as its internal state changes.
The pattern allows the class for an object to apparently change at run-time.

#### Example

```kotlin
sealed class AuthorizationState

object Unauthorized : AuthorizationState()

class Authorized(val userName: String) : AuthorizationState()

class AuthorizationPresenter {

    private var state: AuthorizationState = Unauthorized

    val isAuthorized: Boolean
        get() = when (state) {
            is Authorized -> true
            is Unauthorized -> false
        }

    val userName: String
        get() {
            val state = this.state //val enables smart casting of state
            return when (state) {
                is Authorized -> state.userName
                is Unauthorized -> "Unknown"
            }
        }

    fun loginUser(userName: String) {
        state = Authorized(userName)
    }

    fun logoutUser() {
        state = Unauthorized
    }

    override fun toString() = "User '$userName' is logged in: $isAuthorized"
}
```

#### Usage

```kotlin
val authorizationPresenter = AuthorizationPresenter()

authorizationPresenter.loginUser("admin")
println(authorizationPresenter)

authorizationPresenter.logoutUser()
println(authorizationPresenter)
```

#### Output

```
User 'admin' is logged in: true
User 'Unknown' is logged in: false
```

[Chain of Responsibility](/patterns/src/test/kotlin/ChainOfResponsibility.kt)
-----------------------

The chain of responsibility pattern is used to process varied requests, each of which may be dealt with by a different handler.

#### Example

```kotlin
interface HeadersChain {
    fun addHeader(inputHeader: String): String
}

class AuthenticationHeader(val token: String?, var next: HeadersChain? = null) : HeadersChain {

    override fun addHeader(inputHeader: String): String {
        token ?: throw IllegalStateException("Token should be not null")
        return inputHeader + "Authorization: Bearer $token\n"
            .let { next?.addHeader(it) ?: it }
    }
}

class ContentTypeHeader(val contentType: String, var next: HeadersChain? = null) : HeadersChain {

    override fun addHeader(inputHeader: String): String =
        inputHeader + "ContentType: $contentType\n"
            .let { next?.addHeader(it) ?: it }
}

class BodyPayload(val body: String, var next: HeadersChain? = null) : HeadersChain {

    override fun addHeader(inputHeader: String): String =
        inputHeader + "$body"
            .let { next?.addHeader(it) ?: it }
}
```

#### Usage

```kotlin
//create chain elements
val authenticationHeader = AuthenticationHeader("123456")
val contentTypeHeader = ContentTypeHeader("json")
val messageBody = BodyPayload("Body:\n{\n\"username\"=\"dbacinski\"\n}")

//construct chain
authenticationHeader.next = contentTypeHeader
contentTypeHeader.next = messageBody

//execute chain
val messageWithAuthentication =
    authenticationHeader.addHeader("Headers with Authentication:\n")
println(messageWithAuthentication)

val messageWithoutAuth =
    contentTypeHeader.addHeader("Headers:\n")
println(messageWithoutAuth)
```

#### Output

```
Headers with Authentication:
Authorization: Bearer 123456
ContentType: json
Body:
{
"username"="dbacinski"
}

Headers:
ContentType: json
Body:
{
"username"="dbacinski"
}
```

[Visitor](/patterns/src/test/kotlin/Visitor.kt)
-------

The visitor pattern is used to separate a relatively complex set of structured data classes from the functionality that may be performed upon the data that they hold.

#### Example

```kotlin
interface ReportVisitable {
    fun <R> accept(visitor: ReportVisitor<R>): R
}

class FixedPriceContract(val costPerYear: Long) : ReportVisitable {
    override fun <R> accept(visitor: ReportVisitor<R>): R = visitor.visit(this)
}

class TimeAndMaterialsContract(val costPerHour: Long, val hours: Long) : ReportVisitable {
    override fun <R> accept(visitor: ReportVisitor<R>): R = visitor.visit(this)
}

class SupportContract(val costPerMonth: Long) : ReportVisitable {
    override fun <R> accept(visitor: ReportVisitor<R>): R = visitor.visit(this)
}

interface ReportVisitor<out R> {

    fun visit(contract: FixedPriceContract): R
    fun visit(contract: TimeAndMaterialsContract): R
    fun visit(contract: SupportContract): R
}

class MonthlyCostReportVisitor : ReportVisitor<Long> {

    override fun visit(contract: FixedPriceContract): Long =
        contract.costPerYear / 12

    override fun visit(contract: TimeAndMaterialsContract): Long =
        contract.costPerHour * contract.hours

    override fun visit(contract: SupportContract): Long =
        contract.costPerMonth
}

class YearlyReportVisitor : ReportVisitor<Long> {

    override fun visit(contract: FixedPriceContract): Long =
        contract.costPerYear

    override fun visit(contract: TimeAndMaterialsContract): Long =
        contract.costPerHour * contract.hours

    override fun visit(contract: SupportContract): Long =
        contract.costPerMonth * 12
}
```

#### Usage

```kotlin
val projectAlpha = FixedPriceContract(costPerYear = 10000)
val projectGamma = TimeAndMaterialsContract(hours = 150, costPerHour = 10)
val projectBeta = SupportContract(costPerMonth = 500)
val projectKappa = TimeAndMaterialsContract(hours = 50, costPerHour = 50)

val projects = arrayOf(projectAlpha, projectBeta, projectGamma, projectKappa)

val monthlyCostReportVisitor = MonthlyCostReportVisitor()

val monthlyCost = projects.map { it.accept(monthlyCostReportVisitor) }.sum()
println("Monthly cost: $monthlyCost")
assertThat(monthlyCost).isEqualTo(5333)

val yearlyReportVisitor = YearlyReportVisitor()
val yearlyCost = projects.map { it.accept(yearlyReportVisitor) }.sum()
println("Yearly cost: $yearlyCost")
assertThat(yearlyCost).isEqualTo(20000)
```

#### Output

```
Monthly cost: 5333
Yearly cost: 20000
```

[Mediator](/patterns/src/test/kotlin/Mediator.kt)
-------

Mediator design pattern is used to provide a centralized communication medium between different objects in a system. This pattern is very helpful in an enterprise application where multiple objects are interacting with each other.
#### Example

```kotlin
class ChatUser(private val mediator: ChatMediator, val name: String) {
    fun send(msg: String) {
        println("$name: Sending Message= $msg")
        mediator.sendMessage(msg, this)
    }

    fun receive(msg: String) {
        println("$name: Message received: $msg")
    }
}

class ChatMediator {

    private val users: MutableList<ChatUser> = ArrayList()

    fun sendMessage(msg: String, user: ChatUser) {
        users
            .filter { it != user }
            .forEach {
                it.receive(msg)
            }
    }

    fun addUser(user: ChatUser): ChatMediator =
        apply { users.add(user) }

}
```

#### Usage

```kotlin
val mediator = ChatMediator()
val john = ChatUser(mediator, "John")

mediator
    .addUser(ChatUser(mediator, "Alice"))
    .addUser(ChatUser(mediator, "Bob"))
    .addUser(john)
john.send("Hi everyone!")
```

#### Output

```
John: Sending Message= Hi everyone!
Alice: Message received: Hi everyone!
Bob: Message received: Hi everyone!
```

[Memento](/patterns/src/test/kotlin/Memento.kt)
-------

The memento pattern is a software design pattern that provides the ability to restore an object to its previous state (undo via rollback).

#### Example
```kotlin
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
```

#### Usage
```kotlin
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
```

#### Output
```
Current State: State #3
Second saved state: State #2
First saved state: initial state
```

Creational
==========

> In software engineering, creational design patterns are design patterns that deal with object creation mechanisms, trying to create objects in a manner suitable to the situation. The basic form of object creation could result in design problems or added complexity to the design. Creational design patterns solve this problem by somehow controlling this object creation.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Creational_pattern)

[Builder / Assembler](/patterns/src/test/kotlin/Builder.kt)
----------

The builder pattern is used to create complex objects with constituent parts that must be created in the same order or using a specific algorithm.
An external class controls the construction algorithm.

#### Example

```kotlin
// Let's assume that Dialog class is provided by external library.
// We have only access to Dialog public interface which cannot be changed.

class Dialog {

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

[Factory Method](/patterns/src/test/kotlin/FactoryMethod.kt)
-----------------

The factory pattern is used to replace class constructors, abstracting the process of object generation so that the type of the object instantiated can be determined at run-time.

#### Example

```kotlin
sealed class Country {
    object USA : Country() //Kotlin 1.0 could only be an inner class or object
}

object Spain : Country() //Kotlin 1.1 declared as top level class/object in the same file
class Greece(val someProperty: String) : Country()
data class Canada(val someProperty: String) : Country() //Kotlin 1.1 data class extends other class
//object Poland : Country()

class Currency(
    val code: String
)

object CurrencyFactory {

    fun currencyForCountry(country: Country): Currency =
        when (country) {
            is Greece -> Currency("EUR")
            is Spain -> Currency("EUR")
            is Country.USA -> Currency("USD")
            is Canada -> Currency("CAD")
        }  //try to add a new country Poland, it won't even compile without adding new branch to 'when'
}
```

#### Usage

```kotlin
val greeceCurrency = CurrencyFactory.currencyForCountry(Greece("")).code
println("Greece currency: $greeceCurrency")

val usaCurrency = CurrencyFactory.currencyForCountry(Country.USA).code
println("USA currency: $usaCurrency")

assertThat(greeceCurrency).isEqualTo("EUR")
assertThat(usaCurrency).isEqualTo("USD")
```

#### Output

```
Greece currency: EUR
US currency: USD
UK currency: No Currency Code Available
```

[Singleton](/patterns/src/test/kotlin/Singleton.kt)
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

[Abstract Factory](/patterns/src/test/kotlin/AbstractFactory.kt)
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
val plantFactory = PlantFactory.createFactory<OrangePlant>()
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

[Adapter](/patterns/src/test/kotlin/Adapter.kt)
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

[Decorator](/patterns/src/test/kotlin/Decorator.kt)
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

    // overriding behaviour
    override fun makeLargeCoffee() {
        println("Enhanced: Making large coffee")
        coffeeMachine.makeLargeCoffee()
    }

    // extended behaviour
    fun makeCoffeeWithMilk() {
        println("Enhanced: Making coffee with milk")
        coffeeMachine.makeSmallCoffee()
        println("Enhanced: Adding milk")
    }
}
```

#### Usage

```kotlin
    val normalMachine = NormalCoffeeMachine()
    val enhancedMachine = EnhancedCoffeeMachine(normalMachine)

    // non-overridden behaviour
    enhancedMachine.makeSmallCoffee()
    // overriding behaviour
    enhancedMachine.makeLargeCoffee()
    // extended behaviour
    enhancedMachine.makeCoffeeWithMilk()
```

#### Output

```
Normal: Making small coffee

Enhanced: Making large coffee
Normal: Making large coffee

Enhanced: Making coffee with milk
Normal: Making small coffee
Enhanced: Adding milk
```

[Facade](/patterns/src/test/kotlin/Facade.kt)
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

[Protection Proxy](/patterns/src/test/kotlin/ProtectionProxy.kt)
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



[Composite](/patterns/src/test/kotlin/Composite.kt)
------------------

The composite pattern is used to compose zero-or-more similar 
objects so that they can be manipulated as one object.

#### Example

```kotlin

open class Equipment(private var price: Int, private var name: String) {
    open fun getPrice(): Int = price
}


/*
[composite]
*/

open class Composite(name: String) : Equipment(0, name) {
    val equipments = ArrayList<Equipment>()

    fun add(equipment: Equipment) {
        this.equipments.add(equipment)
    }

    override fun getPrice(): Int {
        return equipments.map { it.getPrice() }.sum()
    }
}


/*
 leafs
*/

class Cabbinet : Composite("cabbinet")
class FloppyDisk : Equipment(70, "Floppy Disk")
class HardDrive : Equipment(250, "Hard Drive")
class Memory : Equipment(280, "Memory")


```

#### Usage

```kotlin
var cabbinet = Cabbinet()
cabbinet.add(FloppyDisk())
cabbinet.add(HardDrive())
cabbinet.add(Memory())
println(cabbinet.getPrice())
```

#### Ouput

```
600
```



Info
====

Descriptions from: [Gang of Four Design Patterns Reference Sheet](http://www.blackwasp.co.uk/GangOfFour.aspx)
