interface Currency {
    fun code(): String
}

class Euro : Currency {
    override fun code(): String {
        return "EUR"
    }
}

class UnitedStatesDolar : Currency {
    override fun code(): String {
        return "USD"
    }
}

enum class Country {
    UnitedStates, Spain, UK, Greece
}

class CurrencyFactory {
    fun currencyForCountry(country: Country): Currency? {
        when (country) {
            Country.Spain, Country.Greece -> return Euro()
            Country.UnitedStates -> return UnitedStatesDolar()
            else -> return null
        }
    }
}

fun main(args: Array<String>) {
    val noCurrencyCode = "No Currency Code Available"

    val greeceCode = CurrencyFactory().currencyForCountry(Country.Greece)?.code() ?: noCurrencyCode
    println("Greece currency: $greeceCode")

    val usCode = CurrencyFactory().currencyForCountry(Country.UnitedStates)?.code() ?: noCurrencyCode
    println("US currency: $usCode")

    val ukCode = CurrencyFactory().currencyForCountry(Country.UK)?.code() ?: noCurrencyCode
    println("UK currency: $ukCode")
}