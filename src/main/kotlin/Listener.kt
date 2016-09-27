interface TextChangedListener {
    fun onTextChanged(newText: String)
}

class PrintingTextChangedListner : TextChangedListener {
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

fun main(args: Array<String>) {
    val textView = TextView()
    textView.listener = PrintingTextChangedListner()
    textView.text = "Lorem ipsum"
    textView.text = "dolor sit amet"
}

