# Design Patterns In Kotlin

Base on: https://github.com/ochococo/Design-Patterns-In-Swift

## Table of Contents

* [Behavioral Patterns](#behavioral)
	* [Strategy](#strategy)
* [Creational Patterns](#creational)
* [Structural Patterns](#structural)

Behavioral
==========

>In software engineering, behavioral design patterns are design patterns that identify common communication patterns between objects and realize these patterns. By doing so, these patterns increase flexibility in carrying out this communication.
>
>**Source:** [wikipedia.org](http://en.wikipedia.org/wiki/Behavioral_pattern)

Observer / Listener
--------

The observer pattern is used to allow an object to publish changes to its state. 
Other objects subscribe to be immediately notified of any changes.

### Example

```
kotlin
```

Strategy
-----------

The strategy pattern is used to create an interchangeable family of algorithms from which the required process is chosen at run-time.

### Example

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

### Usage

```kotlin
    val lowerCasePrinter = Printer(LowerCaseFormatter())
    lowerCasePrinter.printString("LOREM ipsum DOLOR sit amet")

    val upperCasePrinter = Printer(UpperCaseFormatter())
    upperCasePrinter.printString("LOREM ipsum DOLOR sit amet")
```

### Output
```
	lorem ipsum dolor sit amet
	LOREM IPSUM DOLOR SIT AMET
```

### Code
[Strategy.kt](https://github.com/dbacinski/Design-Patterns-In-Kotlin/blob/master/src/main/kotlin/Strategy.kt)


Chain Of Responsibility
-----------------------

The chain of responsibility pattern is used to process varied requests, each of which may be dealt with by a different handler.

### Example:
```
kotlin
```

Command
-------

The command pattern is used to express a request, including the call to be made and all of its required parameters, in a command object. The command may then be executed immediately or held for later use.

### Example:

```
kotlin
```

State
---------

The state pattern is used to alter the behaviour of an object as its internal state changes. 
The pattern allows the class for an object to apparently change at run-time.

### Example

```
kotlin
```

Visitor
----------

The visitor pattern is used to separate a relatively complex set of structured data classes from the functionality that may be performed upon the data that they hold.

### Example

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

### Example

```
kotlin
```

Factory Method
-----------------

The factory pattern is used to replace class constructors, abstracting the process of object generation so that the type of the object instantiated can be determined at run-time.

### Example

```
kotlin
```

Singleton
------------

The singleton pattern ensures that only one object of a particular class is ever created.
All further references to objects of the singleton class refer to the same underlying instance.
There are very few applications, do not overuse this pattern!

### Example:

```
kotlin
```

Abstract Factory
-------------------

The abstract factory pattern is used to provide a client with a set of related or dependant objects. 
The "family" of objects created by the factory are determined at run-time.

### Example

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

### Example

```
kotlin
```

Decorator
------------

The decorator pattern is used to extend or alter the functionality of objects at run- time by wrapping them in an object of a decorator class. 
This provides a flexible alternative to using inheritance to modify behaviour.

### Example

```
kotlin
```

Façade
---------

The facade pattern is used to define a simplified interface to a more complex subsystem.

### Example

```
kotlin
```

Protection Proxy
------------------

The proxy pattern is used to provide a surrogate or placeholder object, which references an underlying object. 
Protection proxy is restricting access.

### Example

```
kotlin
```

Info
====

Descriptions from: [Gang of Four Design Patterns Reference Sheet](http://www.blackwasp.co.uk/GangOfFour.aspx)
