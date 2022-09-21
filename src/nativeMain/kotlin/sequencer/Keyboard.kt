package sequencer

import controller.MidiController
import controller.Pad
import controller.events.PadPressed
import controller.events.PadReleased
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.E1
import midi.core.noteOff
import midi.core.noteOn
import utils.*
import utils.MusicalScale.Companion.MINOR

class Keyboard(
    val controller: MidiController,
    val rootNoteColor: Int = 0x80_40_00,
    val noteColor: Int = 0x80_80_00,
    val playColor: Int = 0xA0_A0_80,
    val rootNote: MidiNote = E1,
    val scale: MusicalScale = MINOR
) : MidiActivity("Keyboard") {

    private val overlays = PropertyOverlays<Pad, Int>(2) { color = it }
    private val colors = overlays[0]
    private val cursor = overlays[1]
    private val notes = mutableMapOf<Pad, MidiNote>()

    init {
        onClock.add(overlays)
    }

    override fun onChange(midi: MidiContext) {
        println("Changing colors")
        controller.pads.forEach { pad ->
            val note = rootNote + scale[pad.col + (controller.pads.lastRow - pad.row) * 3]
            notes[pad] = note
            colors[pad] = when {
                (note.number - rootNote.number) % 12 == 0 -> rootNoteColor
                else -> noteColor
            }
        }
    }

    override fun onEvent(midi: MidiContext, event: MidiEvent) = with(midi) {
        when (event) {
            is PadPressed -> {
                val note = notes[event.pad]!!
                noteOn(note, event.velocity)
                notes.keys
                    .filter { notes[it] == note }
                    .forEach { cursor[it] = playColor }
            }
            is PadReleased -> {
                val note = notes[event.pad]!!
                noteOff(note, event.velocity)
                notes.keys
                    .filter { notes[it] == note }
                    .forEach { cursor[it] = null }
            }
        }
    }
}


