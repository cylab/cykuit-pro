@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.apc40mk2.APC40MK2Controller
import controller.buttons.MuteButton
import controller.fire.FireController
import midi.api.*
import midi.core.*
import sequencer.*
import utils.instanceOf

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
    class Logger(val name: String) : MidiFunImpl ({
        if (it !is SysrtEvent){
            print(justify("[$name]" to -10))
            println(it)
        }
        emit(it)
    })

    midiClock.bpm = 100
    val buttonViews = ButtonMappedActivities(
        "Views",
        controller.buttons.instanceOf<MuteButton>(0) to HomeView(controller),
        controller.buttons.instanceOf<MuteButton>(1) to keyboard
    )
    controllerIn.add(
        MidiPipe(controller.updater, controllerOut),
        MidiPipe(Logger("IN"), controller.mapper, Logger("MAPPED"),
            MidiGroup(songControl, buttonViews), Logger("OUT"), instrumentOut)
    )
}
