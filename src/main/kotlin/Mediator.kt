class ChatUser(val mediator: ChatMediator, val name: String){
    fun send(msg: String) {
        println("${name}: Sending Message= ${msg}")
        mediator.sendMessage(msg, this)
    }

    fun receive(msg: String) {
        println("${name}: Message received: ${msg}")
    }

}

class ChatMediator {
    private val users: MutableList<ChatUser> = ArrayList()

    fun sendMessage(msg: String, user: ChatUser) {
        users.forEach {
            if (it !== user)
                it.receive(msg)
        }
    }

    fun addUser(user: ChatUser) {
        users.add(user)
    }

}

fun main(args: Array<String>) {
    val mediatorImpl = ChatMediator()
    val john = ChatUser(mediatorImpl, "John")

    with(mediatorImpl) {
        addUser(ChatUser(this, "User1"))
        addUser(ChatUser(this, "User2"))
        addUser(ChatUser(this, "User3"))
        addUser(john)
    }

    john.send("Hi everyone!")
}