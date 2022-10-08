package sequencer

import controller.*
import controller.events.PadPressed
import controller.events.PadReleased
import midi.activity.MidiActivity
import midi.api.*
import midi.api.MidiNote.C1
import midi.core.noteOff
import midi.core.noteOn
import utils.MusicalScale
import utils.MusicalScale.Companion.MINOR
import utils.Value

class Keyboard(
    val controller: MidiController,
    colors: TrackColors,
    rootNote: MidiNote = C1,
    scale: MusicalScale = MINOR,
    area: Rect = Rect(0, 0, controller.pads.cols, controller.pads.rows),
    name: String = "unnamed Keyboard"
) : MidiActivity(name) {

    private val base = Layer(this, "Keyboard Base")
    private val overlay = Layer(this, "Keyboard Overlay")
    private val notesByPad = mutableMapOf<Pad, MidiNote>()
    private var highlighted = mutableSetOf<MidiNote>()

    var colors = colors; set(value) {; field = value; changed = true }
    var rootNote = rootNote; set(value) {; field = value; changed = true }
    var scale = scale; set(value) {; field = value; changed = true }
    var area = area; set(value) {; field = value; changed = true }

    fun clearHighlight(note: MidiNote) {
        highlighted.remove(note)
        changed = true
    }

    fun clearHighlight(notes: List<MidiNote>) {
        highlighted.removeAll(notes)
        changed = true
    }

    fun clearHighlights() {
        highlighted.clear()
        changed = true
    }

    fun highlight(note: MidiNote) {
        highlighted.add(note)
        changed = true
    }

    fun highlight(notes: List<MidiNote>) {
        highlighted.addAll(notes)
        changed = true
    }

    // updater to plug in somewhere in the midi routes to highlight the keys based on note-events
    val updater = object : MidiActivity("$name Updater") {
        init {
            onEvent.addFor<NoteOn> { updatePadsByNote(it.note, colors.step) }
            onEvent.addFor<NoteOff> { updatePadsByNote(it.note, null) }
        }
    }

    init {
        onActivate.add { updater.activate(this) }
        onDeactivate.add { updater.deactivate(this) }
        onChange.add {
            mapNotes()
            redraw()
        }
        onEvent.addFor<PadPressed> { event ->
            notesByPad[event.pad]?.let { note ->
                noteOn(note, 110 /* event.velocity */)
                updatePadsByNote(note, colors.step)
            }
        }
        onEvent.addFor<PadReleased> { event ->
            notesByPad[event.pad]?.let { note ->
                noteOff(note, event.velocity)
                updatePadsByNote(note, null)
            }
        }
    }

    private fun mapNotes() {
        notesByPad.clear()
        controller.pads.forEach { pad ->
            if (pad.col in area.x1 until area.x2 && pad.row in area.y1 until area.y2) {
                val col = pad.col - area.x1
                val row = pad.row - area.y1
                val note = rootNote + scale[col + (area.height - 1 - row) * 3]
                notesByPad[pad] = note
            }
        }
    }

    private fun redraw() {
        controller.pads.forEach { pad ->
            notesByPad[pad]
                ?.let { note ->
                    pad.color[base] = when {
                        note in highlighted -> colors.step
                        (note.number - rootNote.number) % 12 == 0 -> colors.root
                        else -> colors.note
                    }
                }
                ?: pad.color.remove(base) // remove the keyboard colors outside of the keyboards area
        }
    }

    private fun updatePadsByNote(note: MidiNote, color: Value<Int?>?, layer: Layer = overlay) = notesByPad.keys
        .filter { notesByPad[it] == note }
        .forEach { it.color[overlay] = color }
}


