package controller

import controller.MidiControlType.FADER
import controller.MidiControlType.KNOB
import controller.MidiControlType.PAD
import controller.MidiControlType.PUSH_BUTTON
import controller.MidiControlType.ROTARY
import controller.MidiControlType.TOGGLE_BUTTON
import midi.api.MidiEvent
import midi.api.MidiNote.C0
import midi.api.NoteOn

enum class MidiControlType {
    PUSH_BUTTON, TOGGLE_BUTTON, FADER, KNOB, ROTARY, PAD
}

sealed class MidiControl(
    val mapping: MidiEvent,
    val name: String = "unnamed",
    val type: MidiControlType = PUSH_BUTTON
) {
    open infix fun matches(event: MidiEvent) = mapping matches event
}

class PushButton(mapping: MidiEvent, name: String = "unnamed") : MidiControl(mapping, name, PUSH_BUTTON)
class ToggleButton(mapping: MidiEvent, name: String = "unnamed") : MidiControl(mapping, name, TOGGLE_BUTTON)
class Fader(mapping: MidiEvent, name: String = "unnamed") : MidiControl(mapping, name, FADER)
class Knob(mapping: MidiEvent, name: String = "unnamed") : MidiControl(mapping, name, KNOB)
class Rotary(mapping: MidiEvent, name: String = "unnamed") : MidiControl(mapping, name, ROTARY)

class Pad(
    mapping: MidiEvent,
    name: String = "unnamed",
    private val onChange: ((MidiControl) -> Unit)? = null
) : MidiControl(mapping, name, PAD) {
    var color: Int = 0x00_00_00
        set(value) {
            if(field != value) {
                field = value
                dirty = true
                onChange?.invoke(this)
            }
        }
    var dirty = true
}

class Pads(
    val cols: Int,
    val rows: Int,
    baseMapping: MidiEvent = NoteOn(C0),
    init: (Int) -> Pad = { Pad(baseMapping.copy(baseMapping.data1 + it), "Pad $it") }
) : List<Pad> by List(cols * rows, init) {

    fun get(col: Int, row: Int) = when ((col in 0 until cols) && (row in 0 until rows)) {
        true -> get(col + row * cols)
        else -> throw IndexOutOfBoundsException("col: $col, row: $row, index: ${col + row * cols}, size: $size")
    }
}

class Buttons(
    val amount: Int,
    baseMapping: MidiEvent = NoteOn(C0),
    init: (Int) -> Button = { Button(baseMapping.copy(baseMapping.data1 + it), "Button $it") }
) : List<Button> by List(amount, init)

class Button(
    mapping: MidiEvent,
    name: String = "unnamed",
    private val onChange: ((MidiControl) -> Unit)? = null
) : MidiControl(mapping, name, PUSH_BUTTON) {
    var value: Int = 0
        set(value) {
            if(field != value) {
                field = value
                dirty = true
                onChange?.invoke(this)
            }
        }
    var dirty = true
}



