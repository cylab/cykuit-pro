@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import controller.Mapping
import controller.apc40mk2.APC40MK2Controller
import controller.buttons.ButtonCommand.*
import controller.fire.FireController
import jack.jackMidiClient
import midi.api.*
import midi.core.*
import sequencer.*
import sequencer.persistence.SaveFileRepository
import sequencer.persistence.model.SequenceSave

fun main() = with(jackMidiClient) {
//    makeColors()
    try {
//        testFileIO()
//        startApc40()
        startFire()
        val readLine = readLine()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        destroy()
    }
}

private fun testFileIO() = with(jackMidiClient) {
    val repo = SaveFileRepository()
    repo.save("some name", SequenceSave(hello="MyWorld!", whatThe = "Fuck!"))
}

private fun startApc40() = with(jackMidiClient) {
    val midiIn = midiIns["APC40"] ?: midiIns.connect("APC40")
    val midiOut = midiOuts["APC40"] ?: midiOuts.connect("APC40")
    midiClock.bpm = 100
    midiIn.add { if (it !is SysrtEvent) println(it) }
    midiIn.add(MidiPipe(APC40MK2Controller(), midiOut))
}

private fun startFire() = with(jackMidiClient) {

    val controllerIn = midiIns.connect("FL STUDIO FIRE")
    val controllerOut = midiOuts.connect("FL STUDIO FIRE")
    val instrumentOut = midiOuts.connect("Wavetable")

    val config = Config()
    val controller = FireController()
    val sequencer = TrackSequencer()

    midiClock.bpm = 100
    with(controller) {
        val rootViews = MappedActivities(
            "Root Views",
            Mapping(buttons[Pattern][0]) to PatternView(config, controller, sequencer),
            Mapping(buttons[Browser][0]) to HomeView(controller),
        )
        controllerIn.add(
            MidiPipe(updater, controllerOut),
            MidiPipe(
                Logger("IN"), mapper, Logger("MAPPED"),
                MidiGroup(
                    SongControl(controller),
                    rootViews,
                    sequencer
                ), instrumentOut
            )
        )
    }
}
