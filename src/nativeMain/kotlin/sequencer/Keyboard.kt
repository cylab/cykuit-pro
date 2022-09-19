package sequencer

import controller.MidiController
import controller.events.PadPressed
import controller.events.PadReleased
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.E1
import midi.core.noteOff
import midi.core.noteOn
import utils.MusicalScale
import utils.MusicalScale.Companion.MINOR
import utils.PropertyListOverlays

class Keyboard(
    val controller: MidiController,
    val rootNoteColor: Int = 0x80_40_00,
    val noteColor: Int = 0x80_80_00,
    val playColor: Int = 0xA0_A0_80,
    val rootNote: MidiNote = E1,
    val scale: MusicalScale = MINOR
) : MidiActivity("Keyboard") {

    private val overlays = PropertyListOverlays(2, controller.pads, { color }, { color = it })
    private val colors = overlays[0]
    private val cursor = overlays[1]
    private val notes = Array(controller.pads.size) { rootNote }

    init {
        onClock.add(overlays)
    }

    override fun onChange(midi: MidiContext) {
        controller.pads.indices.forEach {
            val (col, row) = controller.pads.location(it)
            val note = rootNote + scale[col + (controller.pads.lastRow - row) * 3]
            notes[it] = note
            colors[it] = when {
                (note.number - rootNote.number) % 12 == 0 -> rootNoteColor
                else -> noteColor
            }
        }
    }

    override fun onEvent(midi: MidiContext, event: MidiEvent) = with(midi) {
        when (event) {
            is PadPressed -> {
                val note = notes[event.pad.index]
                noteOn(note, event.velocity)
                notes.indices
                    .filter { notes[it] == note }
                    .forEach { cursor[it] = playColor }
            }
            is PadReleased -> {
                val note = notes[event.pad.index]
                noteOff(note, event.velocity)
                notes.indices
                    .filter { notes[it] == note }
                    .forEach { cursor[it] = null }
            }
        }
    }
}


