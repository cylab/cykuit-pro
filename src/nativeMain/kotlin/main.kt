@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.apc40mk2.APC40MK2Controller
import midi.api.*
import midi.core.*


fun main() {
//    makeColors()
    startApc40()
}

private fun startApc40() {
    try {
        val midi = MidiClient.default

        midi.midiClock.bpm = 100
        midi.midiIn.add { if (it !is SysrtEvent) println(it) }
        midi.midiIn.add(MidiPipe(APC40MK2Controller(), midi.midiOut))

        println("before!")
        val readLine = readLine()
        println("after!")
    } catch (e: Exception) {
        println(e.message)
    } finally {
        println("finally!")
        MidiClient.default.destroy()
    }
    println("end!")
}
