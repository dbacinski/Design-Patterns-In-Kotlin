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

fun main(args: Array<String>) {
    val authenticationHeader = AuthenticationHeader("123456")
    val contentTypeHeader = ContentTypeHeader("json")
    val messageBody = BodyPayload("{\"username\"=\"dbacinski\"}")

    val messageChainWithAuthorization = messageChainWithAuthorization(authenticationHeader, contentTypeHeader, messageBody)
    val messageWithAuthentication = messageChainWithAuthorization.addLines("Message with Authentication:\n")
    println(messageWithAuthentication)

    val messageChainUnauthenticated = messageChainUnauthenticated(contentTypeHeader, messageBody)
    val message = messageChainUnauthenticated.addLines("Message:\n")
    println(message)
}

private fun messageChainWithAuthorization(authenticationHeader: AuthenticationHeader, contentTypeHeader: ContentTypeHeader, messageBody: BodyPayload): MessageChain {
    authenticationHeader.next = contentTypeHeader
    contentTypeHeader.next = messageBody
    return authenticationHeader
}

private fun messageChainUnauthenticated(contentTypeHeader: ContentTypeHeader, messageBody: BodyPayload): MessageChain {
    contentTypeHeader.next = messageBody
    return contentTypeHeader
}



