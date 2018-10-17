interface ChatMediator {
    fun sendMessage(msg: String, user: ChatUser)
    fun addUser(user: ChatUser)
}

abstract class ChatUser(val mediator: ChatMediator, val name: String) {
    abstract fun send(msg: String)
    abstract fun receive(msg: String)
}

class ChatUserImpl(mediator: ChatMediator, name: String) : ChatUser(mediator, name) {
    override fun send(msg: String) {
        println("${name}: Sending Message= ${msg}")
        mediator.sendMessage(msg, this)
    }

    override fun receive(msg: String) {
        println("${name}: Message received: ${msg}")
    }

}

class MediatorImpl : ChatMediator {
    private val users: MutableList<ChatUser> = ArrayList()

    override fun sendMessage(msg: String, user: ChatUser) {
        users.forEach {
            if (it !== user)
                it.receive(msg)
        }
    }

    override fun addUser(user: ChatUser) {
        users.add(user)
    }

}

fun main(args: Array<String>) {
    val mediatorImpl = MediatorImpl()
    val john = ChatUserImpl(mediatorImpl, "John")

    with(mediatorImpl) {
        addUser(ChatUserImpl(this, "User1"))
        addUser(ChatUserImpl(this, "User2"))
        addUser(ChatUserImpl(this, "User3"))
        addUser(john)
    }

    john.send("Hi everyone!")
}