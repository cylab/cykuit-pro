package controller

import controller.buttons.ButtonCommand
import controller.events.ButtonPressed
import controller.events.ButtonReleased
import midi.api.*
import midi.core.half
import utils.*

@Suppress("SetterBackingFieldAssignment")
class Button(
    val command: ButtonCommand,
    val mapPress: ProtocolEvent,
    val mapRelease: ProtocolEvent,
    val number: Int = 0,
    name: String = "$command Button $number",
    val off: Value<Int?> = Value { 0 },
    val inactive: Value<Int?> = off,
    val active: Value<Int?> = Value { 127 },
    val standby: Value<Int?> = blink(1.half, 127, 0),
    val highlight: Value<Int?> = active,
    val state1: Value<Int?> = Value { 1 },
    val state2: Value<Int?> = Value { 2 },
    val state3: Value<Int?> = Value { 3 },
    val state4: Value<Int?> = Value { 4 }
) : MidiControl(name) {

    var dirty = true

    var value: Int = off() ?: 0
        set(value) {
            if (field != value) {
                field = value
                dirty = true
            }
        }

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress matches event -> midi.emit(ButtonPressed(this))
            mapRelease matches event -> midi.emit(ButtonReleased(this))
        }
    }
}
