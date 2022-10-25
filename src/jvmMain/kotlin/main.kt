import javamidi.javaMidiClient
import midi.core.Logger

fun main(): Unit = with(javaMidiClient) {
    try {
        println("Hello World!")
        val controllerIn = midiIns.connect("FIRE")
        controllerIn.add(Logger("IN"))
        val readLine = readLine()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        destroy()
    }
}

