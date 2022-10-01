package controller

import controller.events.*
import midi.api.*

class Pad(
    val number: Int,
    val col: Int,
    val row: Int,
    val mapPress: ProtocolEvent,
    val mapRelease: ProtocolEvent,
    name: String = "unnamed"
) : MidiControl(name) {

    val color = LayeredValue(0x00_00_00)

    fun dirtyAfterUpdate(clock: MidiClock) :Boolean {
        return color.dirtyAfterUpdate(clock).also {
//            if(it) println("\t-> updated $this")
        }
    }

    fun emitMapped(midi: MidiContext, event: MidiEvent) {
        when {
            mapPress matches event -> midi.emit(PadPressed(this, (event as ProtocolEvent).data2))
            mapRelease matches event -> midi.emit(PadReleased(this, (event as ProtocolEvent).data2))
        }
    }
}
