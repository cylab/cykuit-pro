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
) : MidiActivity("ClipEditor $clip") {

    private val base = Layer(this, "$name Base")
    private val overlay = Layer(this, "$name Overlay")
    private val keyboard = Keyboard(controller, colors, name = "$name Keyboard")
    private val playHead = PlayHead(controller)
    private var editedStep: Int? = null

    init {
        clip.onEmit.add { event ->
            when (event) {
                is NoteOn -> keyboard.highlight(event.note)
                is NoteOff -> keyboard.clearHighlight(event.note)
            }
        }
        keyboard.onEmit.addFor<NoteOn> { event ->
            editedStep?.let { step ->
                toggleNoteInStep(event.note, event.velocity, clip.steps[step])
            }
        }

        onProcess.add(
            keyboard,
            playHead
        )
        onClock.add {
            when {
                midiClock.playing && !playHead.active -> playHead.activate()
                !midiClock.playing && playHead.active -> playHead.deactivate()
            }
            if (midiClock.playing) {
                playHead.position = clip.position
            }
        }
        onActivate.add {
            keyboard.activate()
            clip.activate()
        }
        onDeactivate.add {
            keyboard.deactivate()
        }
        onChange.add { redraw() }

        onEvent.addFor<PadPressed> { startEditing(it) }
        onEvent.addFor<PadReleased> { stopEditing(it) }
    }

    private fun redraw() {
        val stepRows = (clip.steps.size - 1) / controller.pads.cols + 1
        controller.pads
            .filter { it.row < stepRows }
            .forEach { pad ->
                val clip = clip.steps.getOrNull(pad.number)
                pad.color[base] = when {
                    clip == null -> colors.off
                    clip.isEmpty() -> colors.empty
                    else -> colors.step
                }
            }
        keyboard.area = Rect(0, stepRows, controller.pads.cols, controller.pads.rows - stepRows)
    }

    private fun MidiContext.startEditing(event: PadPressed) {
        val stepNumber = event.pad.number
        if (stepNumber < clip.steps.size) {
            if (editedStep != null) {
                stopStep(editedStep)
                keyboard.clearHighlights()
            }
            editedStep = stepNumber
            event.pad.color[overlay] = colors.step
            keyboard.highlight(clip.steps[stepNumber].notes.mapNotNull { it?.value })
            playStep(editedStep)
        }
    }

    private fun MidiContext.stopEditing(event: PadReleased) {
        val stepNumber = event.pad.number
        if (stepNumber == editedStep) {
            stopStep(editedStep)
            editedStep = null
            event.pad.color[overlay] = null
            keyboard.clearHighlights()
        }
    }

    private fun MidiContext.playStep(stepNumber: Int?) {
        val step = stepNumber?.let { clip.steps[it] }
        step?.notes?.filterNotNull()?.forEach { noteOn(it.value, it.velocity) }
    }

    private fun MidiContext.stopStep(stepNumber: Int?) {
        val step = stepNumber?.let { clip.steps[it] }
        step?.notes?.filterNotNull()?.forEach { noteOff(it.value, it.velocity) }
    }

    private fun MidiContext.playStep(stepNumber: Int) {
        clip.steps[stepNumber].notes.filterNotNull().forEach { noteOff(it.value, it.velocity) }
    }

    private fun toggleNoteInStep(midiNote: MidiNote, velocity: Int, step: Step) {
        val note = Note(midiNote, velocity, 1.sixteenth)
        val notes = step.notes
        val existing = notes.indexOf(note)
        val freeSlot = notes.indexOf(null)
        when {
            existing >= 0 -> {
                notes[existing] = null
                keyboard.clearHighlight(note.value)
            }
            freeSlot >= 0 -> {
                notes[freeSlot] = note
                keyboard.highlight(note.value)
            }
            // no more free slots, so just replace the first note
            else -> {
                keyboard.clearHighlight(notes[0]!!.value)
                notes[0] = note
                keyboard.highlight(note.value)
            }
        }
        changed = true
    }
}


