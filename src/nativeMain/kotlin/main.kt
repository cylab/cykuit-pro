@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.apc40mk2.APC40MK2Controller
import controller.fire.FireController
import midi.api.*
import midi.core.*
import sequencer.Keyboard
import sequencer.SongControl

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
    val midiIn = midiIns["APC40"] ?: midiIns.connect("APC40")
    val midiOut = midiOuts["APC40"] ?: midiOuts.connect("APC40")
    midiClock.bpm = 100
    midiIn.add { if (it !is SysrtEvent) println(it) }
    midiIn.add(MidiPipe(APC40MK2Controller(), midiOut))
}

private fun startFire() = with(MidiClient.default) {

    val controllerIn = midiIns.connect("FL STUDIO FIRE")
    val controllerOut = midiOuts.connect("FL STUDIO FIRE")
    val instrumentOut = midiOuts.connect("Wavetable")

    val controller = FireController()
    val keyboard = Keyboard(controller)
    val songControl = SongControl(controller)
    val logger = MidiFun {
        if (it !is SysrtEvent) println(it)
        emit(it)
    }

    midiClock.bpm = 100

    controllerIn.add(
        MidiPipe(controller.updater, controllerOut),
        MidiPipe(logger, controller.mapper, logger, MidiGroup(songControl, keyboard), logger, instrumentOut)
    )
}
