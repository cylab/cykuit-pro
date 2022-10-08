package controller

import controller.buttons.ButtonCommand
import controller.events.ButtonPressed
import controller.events.ButtonReleased
import midi.api.*
import midi.core.half
import utils.Value
import utils.blink

@Suppress("SetterBackingFieldAssignment")
class Button(
    val command: ButtonCommand,
    val mapPress: ProtocolEvent,
    val mapRelease: ProtocolEvent,
    val number: Int = 0,
    name: String = "$command Button $number",
    val OFF: Value<Int?> = Value { 0 },
    val INACTIVE: Value<Int?> = OFF,
    val ACTIVE: Value<Int?> = Value { 127 },
    val STANDBY: Value<Int?> = blink(1.half, 127, 0),
    val HIGHLIGHT: Value<Int?> = ACTIVE,
    val STATE1: Value<Int?> = Value { 1 },
    val STATE2: Value<Int?> = Value { 2 },
    val STATE3: Value<Int?> = Value { 3 },
    val STATE4: Value<Int?> = Value { 4 }
) : MidiControl(name) {

    override var down = false; private set

    var dirty = true

    var state: Int = OFF() ?: 0
        set(value) {
            if (field != value) {
                field = value
                dirty = true
            }
        }

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress matches event -> {
                down = true
                midi.emit(ButtonPressed(this))
            }
            mapRelease matches event -> {
                down = false
                midi.emit(ButtonReleased(this))
            }
        }
    }
}
