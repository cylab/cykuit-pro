@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.apc40mk2.APC40MK2Controller
import controller.buttons.ButtonCommand.*
import controller.buttons.ButtonCommand.Note
import controller.fire.FireController
import midi.api.*
import midi.core.*
import sequencer.*

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

    val config = Config()
    val controller = FireController()
    val sequencer = TrackSequencer()
    val keyboard = Keyboard(controller, config.trackColors[0], area = Rect(4,2,8,2))

    midiClock.bpm = 100
    with(controller) {
        val rootViews = MappedActivities(
            "Root Views",
            buttons[Pattern][0] to PatternView(config, controller, sequencer),
            buttons[Generic][0] to HomeView(controller),
        )
        val clipEditor = ClipEditor(
            controller,
            clip = sequencer.grid[0][0],
            colors = config.trackColors[0],
        )
        controllerIn.add(
            MidiPipe(updater, controllerOut),
            MidiPipe(
                Logger("IN"), mapper, Logger("MAPPED"),
                MidiGroup(
                    SongControl(controller),
                    rootViews
                ), Logger("OUT"), instrumentOut
            )
        )
    }
}

class Logger(val name: String) : MidiFunImpl ({
    if (it !is SysrtEvent){
        print(justify("[$name]" to -10))
        println(it)
    }
    emit(it)
})
