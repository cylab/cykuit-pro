package controller

import controller.events.PadPressed
import controller.events.PadReleased
import midi.api.*

class Pad(
    val number: Int,
    val col: Int,
    val row: Int,
    val mapPress: ProtocolEvent,
    val mapRelease: ProtocolEvent,
    name: String = "unnamed"
) : MidiControl(name) {

    override var down = false; private set

    val color = LayeredValue(0x00_00_00)

    fun dirtyAfterUpdate(clock: MidiClock) :Boolean {
        return color.dirtyAfterUpdate(clock).also {
//            if(it) println("\t-> updated $this")
        }
    }

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress matches event -> {
                down = true
                midi.emit(PadPressed(this, (event as ProtocolEvent).data2))
            }
            mapRelease matches event -> {
                down = false
                midi.emit(PadReleased(this, (event as ProtocolEvent).data2))
            }
        }
    }
}
