import kotlin.properties.Delegates

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

fun main(args: Array<String>) {
    val textView = TextView()
    textView.listener = PrintingTextChangedListener()
    textView.text = "Lorem ipsum"
    textView.text = "dolor sit amet"
}

