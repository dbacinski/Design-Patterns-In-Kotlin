import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates

interface TextChangedListener {

    fun onTextChanged(oldText: String, newText: String)
}

class PrintingTextChangedListener : TextChangedListener {

    var text = ""

    override fun onTextChanged(oldText: String, newText: String) {
        text = "Text is changed: $oldText -> $newText"
    }
}

class TextView {

    val listeners = mutableListOf<TextChangedListener>()

    var text: String by Delegates.observable("<empty>") { _, old, new ->
        listeners.forEach { it.onTextChanged(old, new) }
    }
}

class ListenerTest {

    @Test
    fun Listener() {
        val listener = PrintingTextChangedListener()

        val textView = TextView().apply {
            listeners.add(listener)
        }

        with(textView) {
            text = "Lorem ipsum"
            text = "dolor sit amet"
        }

        assertThat(listener.text).isEqualTo("Text is changed: Lorem ipsum -> dolor sit amet")
    }
}

