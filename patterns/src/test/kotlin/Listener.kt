import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

interface TextChangedListener {

    fun onTextChanged(oldText: String, newText: String)
}

class PrintingTextChangedListener : TextChangedListener {

    override fun onTextChanged(oldText: String, newText: String) =
        println("Text is changed $oldText -> $newText")
}

class TextView {

    var listener: TextChangedListener? = null

    var text: String by Delegates.observable("<empty>") { _, old, new ->
        listener?.onTextChanged(old, new)
    }
}

class ListenerTest {

    @Test
    fun `Listener`() {
        val textView = TextView().apply {
            listener = PrintingTextChangedListener()
        }

        with(textView) {
            text = "Lorem ipsum"
            text = "dolor sit amet"
        }

        assertThat(textView.listener).isNotNull()
    }
}

