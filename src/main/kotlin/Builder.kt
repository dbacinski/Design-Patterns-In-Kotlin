import java.io.File

class Dialog() {

    fun showTitle() = println("showing title")

    fun setTitle(title: String) = println("setting title $title")

    fun showMessage() = println("showing message")

    fun setMessage(message: String) = println("setting message $message")

    fun showImage(bitmapBytes: ByteArray) = println("showing image with size ${bitmapBytes.size}")
}

class DialogBuilder(var title: String? = null, var message: String? = null, var image: File? = null) {

    fun build(): Dialog {
        val dialog = Dialog()

        title?.let {
            dialog.setTitle(it)
            dialog.showTitle()
        }

        message?.let {
            dialog.setMessage(it)
            dialog.showMessage()
        }

        image?.apply {
            dialog.showImage(readBytes())
        }

        return dialog
    }
}

fun main(args: Array<String>) {

    val dialog = DialogBuilder()
            .apply {
                title = "Dialog Title"
                message = "Dialog Message"
                image = File.createTempFile("image", "jpg")
            }
            .build()
}

