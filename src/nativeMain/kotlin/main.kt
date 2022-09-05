@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.apc40mk2.APC40MK2Controller
import controller.fire.FireController
import midi.api.*
import midi.core.*

fun main() = with(MidiClient.default) {
//    makeColors()
    try {
//        startApc40()
        startFire()
        val readLine = readLine()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        destroy()
    }
}

private fun startApc40() = with(MidiClient.default) {
    val midiIn = midiIns.connect("APC40")
    val midiOut = midiOuts.connect("APC40")
    midiClock.bpm = 100
    midiIn.add { if (it !is SysrtEvent) println(it) }
    midiIn.add(MidiPipe(APC40MK2Controller(), midiOut))
}

private fun startFire() = with(MidiClient.default) {
    val midiIn = midiIns.connect("FL STUDIO FIRE")
    val midiOut = midiOuts.connect("FL STUDIO FIRE")
    midiClock.bpm = 100
    midiIn.add { if (it !is SysrtEvent) println(it) }
    midiIn.add(MidiPipe(FireController(), midiOut))
}
