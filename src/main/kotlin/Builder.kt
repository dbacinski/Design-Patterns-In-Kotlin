import java.io.File

class Dialog() {

    fun showTitle() = println("showing title");

    fun setTitle(title: String) = println("setting title ${title}")

    fun showMessage() = println("showing message");

    fun setMessage(message: String) = println("setting message ${message}")

    fun showImage(bitmapBytes: ByteArray) = println("showing image with size ${bitmapBytes.size}")
}

class DialogBuilder(var title: String? = null, var message: String? = null, var image: File? = null) {

    fun build(): Dialog {
        val dialog = Dialog()

        if (title != null) {
            dialog.setTitle(title!!)
            dialog.showTitle()
        }

        if (message != null) {
            dialog.setMessage(message!!)
            dialog.showMessage()
        }

        if (image != null) {
            dialog.showImage(image!!.readBytes())
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

