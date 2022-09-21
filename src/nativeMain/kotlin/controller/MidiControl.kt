package controller

import controller.MidiControlType.FADER
import controller.MidiControlType.KNOB
import controller.MidiControlType.PAD
import controller.MidiControlType.PUSH_BUTTON
import controller.MidiControlType.ROTARY
import controller.MidiControlType.TOGGLE_BUTTON
import controller.buttons.GenericButton
import controller.events.*
import midi.api.*
import midi.api.MidiNote.C0

enum class MidiControlType {
    PUSH_BUTTON, TOGGLE_BUTTON, FADER, KNOB, ROTARY, PAD
}

sealed class MidiControl(
    name: String? = null,
    val type: MidiControlType = PUSH_BUTTON
) {
    val name: String = name ?: this::class.simpleName!!
}

class PushButton(mapping: ProtocolEvent, name: String = "unnamed") : MidiControl(name, PUSH_BUTTON)
class ToggleButton(mapping: ProtocolEvent, name: String = "unnamed") : MidiControl(name, TOGGLE_BUTTON)
class Fader(mapping: ProtocolEvent, name: String = "unnamed") : MidiControl(name, FADER)
class Knob(mapping: ProtocolEvent, name: String = "unnamed") : MidiControl(name, KNOB)
class Rotary(mapping: ProtocolEvent, name: String = "unnamed") : MidiControl(name, ROTARY)

class Pad(
    val number: Int,
    val col: Int,
    val row: Int,
    val mapPress: Pair<ProtocolEvent, Pad.(ProtocolEvent) -> ControlEvent>,
    val mapRelease: Pair<ProtocolEvent, Pad.(ProtocolEvent) -> ControlEvent>,
    name: String = "unnamed"
) : MidiControl(name, PAD) {
    var color: Int = 0x00_00_00
        set(value) {
            if (field != value) {
                field = value
                dirty = true
            }
        }
    var dirty = true

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress.first matches event -> midi.emit(mapPress.second.invoke(this, event as ProtocolEvent))
            mapRelease.first matches event -> midi.emit(mapRelease.second.invoke(this, event as ProtocolEvent))
        }
    }
}

class Pads(
    val cols: Int,
    val rows: Int,
    mapPressBase: ProtocolEvent = NoteOn(C0),
    mapReleaseBase: ProtocolEvent = NoteOn(C0),
    init: (Int) -> Pad = { index ->
        val col = index % cols
        val row = index / cols
        Pad(
            index, col, row,
            mapPressBase.copy(mapPressBase.data1 + index) to { PadPressed(this, it.data2, it.defer) },
            mapReleaseBase.copy(mapReleaseBase.data1 + index) to { PadReleased(this, it.data2, it.defer) },
            "Pad ($col,$row)"
        )
    }
) : List<Pad> by List(cols * rows, init) {

    fun location(index: Int): Pair<Int, Int> = when (index in 0 until size) {
        true -> index % cols to index / cols
        else -> throw IndexOutOfBoundsException("index: ${index}, size: $size")
    }

    fun get(col: Int, row: Int) = when ((col in 0 until cols) && (row in 0 until rows)) {
        true -> get(col + row * cols)
        else -> throw IndexOutOfBoundsException("col: $col, row: $row, index: ${col + row * cols}, size: $size")
    }

    val dirtyPads
        get() = filter { it.dirty }.onEach { it.dirty = false }

    val lastCol = cols - 1
    val lastRow = rows - 1
}

class Buttons(buttons: List<Button>) : List<Button> by buttons {
    constructor(vararg buttons: Button) : this(buttons.asList())
    constructor(
        amount: Int,
        mapPressBase: ProtocolEvent = NoteOn(C0),
        mapReleaseBase: ProtocolEvent = NoteOn(C0),
        init: (Int) -> Button = { number ->
            GenericButton(
                number,
                mapPressBase.copy(mapPressBase.data1 + number),
                mapReleaseBase.copy(mapReleaseBase.data1 + number),
                "Button $number"
            )
        }
    ) : this(List(amount, init))

    val dirtyControls
        get() = filter { it.dirty }.onEach { it.dirty = false }
}

open class Button(
    val number: Int,
    val mapPress: Pair<ProtocolEvent, Button.(ProtocolEvent) -> ControlEvent>,
    val mapRelease: Pair<ProtocolEvent, Button.(ProtocolEvent) -> ControlEvent>,
    name: String = "unnamed"
) : MidiControl(name, PUSH_BUTTON) {
    var value: Int = 0
        set(value) {
            if (field != value) {
                field = value
                dirty = true
            }
        }
    var dirty = true

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress.first matches event -> midi.emit(mapPress.second.invoke(this, event as ProtocolEvent))
            mapRelease.first matches event -> midi.emit(mapRelease.second.invoke(this, event as ProtocolEvent))
        }
    }
}
