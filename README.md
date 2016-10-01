# Design Patterns In Kotlin

Project maintained by [@dbacinski](http://twitter.com/dbacinski) (Dariusz Baciński)

Based on [Design-Patterns-In-Swift](https://github.com/ochococo/Design-Patterns-In-Swift) by [@nsmeme](http://twitter.com/nsmeme) (Oktawian Chojnacki)

## Table of Contents

* [Behavioral Patterns](#behavioral)
	* [Observer / Listener](#observer--listener)
	* [Strategy](#strategy)
	* [Command](#command)
	* [State](#state)
	* [Chain of Responsibility](#chain-of-responsibility)
* [Creational Patterns](#creational)
* [Structural Patterns](#structural)

Behavioral
==========

>In software engineering, behavioral design patterns are design patterns that identify common communication patterns between objects and realize these patterns. By doing so, these patterns increase flexibility in carrying out this communication.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Behavioral_pattern)

[Observer / Listener](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/Listener.kt)
--------

The observer pattern is used to allow an object to publish changes to its state. 
Other objects subscribe to be immediately notified of any changes.

#### Example

```kotlin
interface TextChangedListener {
    fun onTextChanged(newText: String)
}

class PrintingTextChangedListner : TextChangedListener{
    override fun onTextChanged(newText: String) {
        println("Text is changed to: $newText")
    }
}

class TextView {

    var listener: TextChangedListener? = null

    var text: String = ""
        set(value) {
            field = value
            listener?.onTextChanged(value)
        }
}
```

#### Usage

```kotlin
    val textView = TextView()
    textView.listener = PrintingTextChangedListner()
    textView.text = "Lorem ipsum"
    textView.text = "dolor sit amet"
```

#### Output

```
    Text is changed to: Lorem ipsum
    Text is changed to: dolor sit amet
```

[Strategy](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/Strategy.kt)
-----------

The strategy pattern is used to create an interchangeable family of algorithms from which the required process is chosen at run-time.

#### Example

```kotlin
interface StringFormatter {
    fun formatString(string: String): String
}

class Printer(val strategy: StringFormatter) {

    fun printString(string: String) {
        println(strategy.formatString(string));
    }
}

class UpperCaseFormatter : StringFormatter {

    override fun formatString(string: String): String {
        return string.toUpperCase()
    }
}

class LowerCaseFormatter : StringFormatter {

    override fun formatString(string: String): String {
        return string.toLowerCase()
    }
}
```

#### Usage

```kotlin
    val lowerCasePrinter = Printer(LowerCaseFormatter())
    lowerCasePrinter.printString("LOREM ipsum DOLOR sit amet")

    val upperCasePrinter = Printer(UpperCaseFormatter())
    upperCasePrinter.printString("LOREM ipsum DOLOR sit amet")
```

#### Output
```
    lorem ipsum dolor sit amet
    LOREM IPSUM DOLOR SIT AMET
```

[Command](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/Command.kt)
-------

The command pattern is used to express a request, including the call to be made and all of its required parameters, in a command object. The command may then be executed immediately or held for later use.

#### Example:

```kotlin
interface OrderCommand {
    fun execute()
}

class OrderAddCommand(val id: Long) : OrderCommand {
    override fun execute() {
        println("adding order with id: $id")
    }
}

class OrderPayCommand(val id: Long) : OrderCommand {
    override fun execute() {
        println("paying for order with id: $id")
    }
}

class CommandProcessor {

    private val queue = ArrayList<OrderCommand>()

    fun addToQueue(orderCommand: OrderCommand): CommandProcessor {
        queue.add(orderCommand)
        return this;
    }

    fun processCommands(): CommandProcessor {
        queue.forEach { it.execute() }
        queue.clear()
        return this;
    }
}
```

#### Usage:
```kotlin
    CommandProcessor()
            .addToQueue(OrderAddCommand(1L))
            .addToQueue(OrderAddCommand(2L))
            .addToQueue(OrderPayCommand(2L))
            .addToQueue(OrderPayCommand(1L))
            .processCommands()
```

#### Output:
```
    adding order with id: 1
	adding order with id: 2
	paying for order with id: 2
	paying for order with id: 1
```

[State](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/State.kt)
------

The state pattern is used to alter the behaviour of an object as its internal state changes. 
The pattern allows the class for an object to apparently change at run-time.

#### Example

```kotlin
interface AuthorizationState {
    fun isAuthorized(): Boolean
    fun userId(): String?
}

class UnauthorizedState() : AuthorizationState {
    override fun isAuthorized(): Boolean {
        return false
    }

    override fun userId(): String? {
        return null
    }
}

class AuthorizedState(val userName: String?) : AuthorizationState {
    override fun isAuthorized(): Boolean {
        return true
    }

    override fun userId(): String? {
        return userName
    }
}

class Authorization {
    private var state: AuthorizationState = UnauthorizedState()

    var isAuthorized: Boolean = false
        get() {
            return state.isAuthorized()
        }

    var userLogin: String? = null
        get() {
            return state.userId()
        }

    fun loginUser(userLogin: String) {
        state = AuthorizedState(userLogin)
    }

    fun logoutUser(userId: String) {
        state = UnauthorizedState()
    }

    override fun toString(): String {
        return "User '${userLogin}' is logged in: ${isAuthorized}"
    }
}
```

#### Usage

```kotlin
    val authorization = Authorization()
    authorization.loginUser("admin")
    println(authorization.toString())
    authorization.logoutUser("admin")
    println(authorization.toString())
```
#### Output

```kotlin
    User 'admin' is logged in: true
    User 'null' is logged in: false
```

[Chain Of Responsibility](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/ChainOfResponsibility.kt)
-----------------------

The chain of responsibility pattern is used to process varied requests, each of which may be dealt with by a different handler.

#### Example:
```kotlin
interface MessageChain {

    fun addLines(inputHeader: String): String
}

class AuthenticationHeader(val token: String?, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String {

        assert(token != null) //break the chain if token is missing

        val header: String = "$inputHeader Authorization: Bearer $token\n"

        return next?.addLines(header) ?: header;
    }
}

class ContentTypeHeader(val contentType: String, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String {

        val header: String = "$inputHeader ContentType: $contentType\n"

        return next?.addLines(header) ?: header;
    }
}

class BodyPayload(val body: String, var next: MessageChain? = null) : MessageChain {

    override fun addLines(inputHeader: String): String {

        val header: String = "$inputHeader $body\n"

        return next?.addLines(header) ?: header;
    }
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

```kotlin
    Message with Authentication:
     Authorization: Bearer 123456
     ContentType: json
     {"username"="dbacinski"}
```

Visitor
----------

The visitor pattern is used to separate a relatively complex set of structured data classes from the functionality that may be performed upon the data that they hold.

#### Example

```
kotlin
```

Creational
==========

> In software engineering, creational design patterns are design patterns that deal with object creation mechanisms, trying to create objects in a manner suitable to the situation. The basic form of object creation could result in design problems or added complexity to the design. Creational design patterns solve this problem by somehow controlling this object creation.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Creational_pattern)


Builder / Assembler
----------

The builder pattern is used to create complex objects with constituent parts that must be created in the same order or using a specific algorithm. 
An external class controls the construction algorithm.

#### Example

```
kotlin
```

Factory Method
-----------------

The factory pattern is used to replace class constructors, abstracting the process of object generation so that the type of the object instantiated can be determined at run-time.

#### Example

```
kotlin
```

Singleton
------------

The singleton pattern ensures that only one object of a particular class is ever created.
All further references to objects of the singleton class refer to the same underlying instance.
There are very few applications, do not overuse this pattern!

#### Example:

```
kotlin
```

Abstract Factory
-------------------

The abstract factory pattern is used to provide a client with a set of related or dependant objects. 
The "family" of objects created by the factory are determined at run-time.

#### Example

```
kotlin

```

Structural
==========

>In software engineering, structural design patterns are design patterns that ease the design by identifying a simple way to realize relationships between entities.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Structural_pattern)

Adapter
----------

The adapter pattern is used to provide a link between two otherwise incompatible types by wrapping the "adaptee" with a class that supports the interface required by the client.

#### Example

```
kotlin
```

Decorator
------------

The decorator pattern is used to extend or alter the functionality of objects at run- time by wrapping them in an object of a decorator class. 
This provides a flexible alternative to using inheritance to modify behaviour.

#### Example

```
kotlin
```

Façade
---------

The facade pattern is used to define a simplified interface to a more complex subsystem.

#### Example

```
kotlin
```

Protection Proxy
------------------

The proxy pattern is used to provide a surrogate or placeholder object, which references an underlying object. 
Protection proxy is restricting access.

#### Example

```
kotlin
```

Info
====

Descriptions from: [Gang of Four Design Patterns Reference Sheet](http://www.blackwasp.co.uk/GangOfFour.aspx)
