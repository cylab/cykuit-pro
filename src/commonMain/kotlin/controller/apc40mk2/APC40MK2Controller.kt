package controller.apc40mk2

import controller.apc40mk2.APC40Color.Companion.blue
import controller.apc40mk2.APC40Color.Companion.brown
import controller.apc40mk2.APC40Color.Companion.cyan
import controller.apc40mk2.APC40Color.Companion.grey
import midi.api.MidiNote.*
import midi.activity.MidiActivity
import midi.activity.SwitchableMidiActivity
import midi.api.*
import midi.core.*


class APC40MK2Controller : MidiActivity("APC40 MKII") {

    private val pads: List<List<Int>> = (Gs0.number..Ds1.number).map { row0 -> (0..4).map { row0 - it * 8 } }

    private val viewSelector = SwitchableMidiActivity()
    private val sceneView = ExampleMidiActivity("sceneView", As4)
    private val patternView = ExampleMidiActivity("patternView", B4)
    private val sendView = ExampleMidiActivity("sendView", C5)

    override fun onStartup(midi: MidiContext) = with(midi) {
        pads.forEach { column -> column.forEach { noteOff(it) } }
    }

    override fun onClock(midi: MidiContext, event: MidiEvent) = with(midi) {
        if(midiClock.ticks % 1.sixteenth != 0)
            return

        val beatNum = (midiClock.position / 1.sixteenth) % 16

        (0..2).forEach { row ->
            listOf(0, 7).forEach { col -> noteOn(pads[col][row], cyan(0)) }
            (1..6).forEach { col -> noteOn(pads[col][row], blue(0)) }
        }
        (3..4).forEach { row ->
            (0..7).forEach { col -> noteOn(pads[col][row], brown(0)) }
        }
        noteOn(pads[0][3], brown())
        noteOn(pads[3][3], brown())
        noteOn(pads[0][4], brown())
        noteOn(pads[6][4], brown())

        if(!midiClock.playing && midiClock.ticks / 1.beat %2 == 0 ) {
            noteOn(pads[0][3], grey())
        }

        if(midiClock.playing) {
            noteOn(pads[beatNum%8][3+beatNum/8], grey())
        }
    }

    override fun onEvent(midi: MidiContext, event: MidiEvent) : Unit = with(midi){
        viewSelector.process(midi, event)
        when {
            event.isNoteOn(As4) -> viewSelector.switchTo(midi, sceneView)
            event.isNoteOn(B4) -> viewSelector.switchTo(midi, patternView)
            event.isNoteOn(C5) -> viewSelector.switchTo(midi, sendView)
            event.isNoteOn(G5) -> midiClock.startPlay()
            event.isNoteOff(G5) -> midiClock.stopPlay()
        }
    }
}

class ExampleMidiActivity(
    name: String,
    private val binding: MidiNote
) : MidiActivity(name) {

    init {
        onEvent.add { if (it.isNoteOff(binding)) noteOn(binding) }
    }

    override fun onStartup(midi: MidiContext) =
        println("$name -> onStartup: ${midi.midiClock.ticks}")

    override fun onEvent(midi: MidiContext, event: MidiEvent) =
        println("$name -> onEvent: ${midi.midiClock.ticks}")

    override fun onActivate(midi: MidiContext) = with(midi) {
        noteOn(binding)
        println("$name -> onActivate: ${midiClock.ticks}")
    }

    override fun onDeactivate(midi: MidiContext) = with(midi) {
        noteOff(binding)
        println("$name -> onDeactivate: ${midiClock.ticks}")
    }
}
