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
        return when (country) {
            Country.Spain, Country.Greece -> Euro()
            Country.UnitedStates          -> UnitedStatesDollar()
            else                          -> null
        }
    }
}

fun main(args: Array<String>) {
    val noCurrencyCode = "No Currency Code Available"

    val greeceCode = CurrencyFactory().currencyForCountry(Country.Greece)?.code ?: noCurrencyCode
    println("Greece currency: $greeceCode")

    val usCode = CurrencyFactory().currencyForCountry(Country.UnitedStates)?.code ?: noCurrencyCode
    println("US currency: $usCode")

    val ukCode = CurrencyFactory().currencyForCountry(Country.UK)?.code ?: noCurrencyCode
    println("UK currency: $ukCode")
}