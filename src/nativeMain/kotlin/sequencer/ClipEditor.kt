package sequencer

import controller.Layer
import controller.MidiController
import controller.events.PadPressed
import controller.events.PadReleased
import midi.activity.MidiActivity
import midi.api.*
import midi.core.*

class ClipEditor(
    val controller: MidiController,
    val clip: Clip,
    val colors: TrackColors,
) : MidiActivity("ClipEditor") {

    private val base = Layer(this, "$name Base")
    private val overlay = Layer(this, "$name Overlay")
    private val keyboard = Keyboard(controller, colors, name = "$name Keyboard")
    private var selectedStep: Int? = null

    init {
        onProcess.add(MidiPipe(
            keyboard,
            MidiPeek<NoteOn> { event ->
                selectedStep?.let { step ->
                    toggleNoteInStep(event.note, event.velocity, clip.steps[step])
                }
            }
        ))
        onActivate.add { keyboard.activate(this) }
        onDeactivate.add { keyboard.deactivate(this) }
        onChange.add { redraw() }

        onEvent.addFor<PadPressed> { selectStep(it) }
        onEvent.addFor<PadReleased> { unselectStep(it) }
    }

    private fun redraw() {
        val stepRows = (clip.steps.size - 1) / controller.pads.cols + 1
        keyboard.area = Rect(0, stepRows, controller.pads.cols, controller.pads.rows - stepRows)
        controller.pads
            .filter { it.row < stepRows }
            .forEach { pad ->
                val clip = clip.steps.getOrNull(pad.number)
                pad.color[base] = when {
                    clip == null -> 0
                    clip.isEmpty() -> colors.empty
                    else -> colors.step
                }
            }
    }

    private fun selectStep(event: PadPressed) {
        if (event.pad.number < clip.steps.size) {
            if (selectedStep != null) keyboard.clearHighlights()
            selectedStep = event.pad.number
            event.pad.color[overlay] = colors.step
            keyboard.highlight(clip.steps[event.pad.number].notes.mapNotNull { it?.value })
        }
    }

    private fun unselectStep(event: PadReleased) {
        if (event.pad.number == selectedStep) {
            selectedStep = null
            event.pad.color[overlay] = null
            keyboard.clearHighlights()
        }
    }

    private fun toggleNoteInStep(midiNote: MidiNote, velocity: Int, step: Step) {
        val note = Note(midiNote, velocity, 1.sixteenth)
        val notes = step.notes
        val existing = notes.indexOf(note);
        when {
            existing >= 0 -> {
                notes[existing] = null
                keyboard.clearHighlight(note.value)
            }
            else -> {
                when (val freeSlot = notes.indexOf(null)) {
                    -1 -> {
                        keyboard.clearHighlight(notes[0]!!.value)
                        notes[0] = note
                    }
                    else -> notes[freeSlot] = note
                }
                keyboard.highlight(note.value)
            }
        }
        changed = true
    }
}


