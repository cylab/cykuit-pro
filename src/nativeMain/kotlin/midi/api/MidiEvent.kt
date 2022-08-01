package midi.api

import midi.api.EventType.*
import midi.core.justify

sealed class MidiEvent(
    val type: EventType,
    val channel: Int = 0,
    val data1: Int = 0,
    val data2: Int = 0,
    val defer: Int = 0
) {
    val data0 = (type.code + (channel and 0b0000_1111)) and 0b1111_1111

    override fun toString() =
        "$type\tchannel:$channel\tdata1:$data1\tdata2:$data2" + if (defer != 0) "\tdefer:$defer" else ""
}


// also see https://www.midi.org/specifications-old/item/table-1-summary-of-midi-message
enum class EventType(val code: Int) {
    NOTE_ON(0b1001_0000),
    NOTE_OFF(0b1000_0000),
    PITCH_BEND(0b1110_0000),
    AFTERTOUCH(0b1101_0000),
    POLY_AFTERTOUCH(0b1010_0000),
    CONTROL_CHANGE(0b1011_0000),
    PROGRAM_CHANGE(0b1100_0000),
    SYSRT_CLOCK(0b1111_1000),
    SYSRT_START(0b1111_1010),
    SYSRT_CONTINUE(0b1111_1011),
    SYSRT_STOP(0b1111_1100),
    SYSRT_SENSING(0b1111_1110),
    SYSRT_RESET(0b1111_1111),
    SYSEX(0b1_1111_0000), // this is a hack! TODO: either document or make unhacky
    NOOP(0); // this is a hack!

    companion object {
        fun fromMessageHead(bits: Int): EventType? {
            val code = when {
                bits and 0b1111_0000 == 0b1111_0000 -> bits
                else -> bits and 0b1111_0000
            }
            return when (code) {
                NOTE_ON.code -> NOTE_ON
                NOTE_OFF.code -> NOTE_OFF
                PITCH_BEND.code -> PITCH_BEND
                AFTERTOUCH.code -> AFTERTOUCH
                POLY_AFTERTOUCH.code -> POLY_AFTERTOUCH
                CONTROL_CHANGE.code -> CONTROL_CHANGE
                PROGRAM_CHANGE.code -> PROGRAM_CHANGE
                SYSRT_CLOCK.code -> SYSRT_CLOCK
                SYSRT_START.code -> SYSRT_START
                SYSRT_CONTINUE.code -> SYSRT_CONTINUE
                SYSRT_STOP.code -> SYSRT_STOP
                SYSRT_SENSING.code -> SYSRT_SENSING
                SYSRT_RESET.code -> SYSRT_RESET
                else -> null
            }
        }
    }
}

sealed class SysrtEvent(type: EventType) : MidiEvent(type) {
    override fun toString() = justify(
        type to -15,
        "defer:" to 10, defer to 5
    )
}

sealed class NoteEvent(type: EventType, channel: Int, val note: MidiNote, data2: Int = 0, defer: Int = 0) :
    MidiEvent(type, channel, data1 = note.number, data2 = data2, defer = defer)


class NoteOn(channel: Int, note: MidiNote, velocity: Int = 127, defer: Int = 0) :
    NoteEvent(NOTE_ON, channel, note, data2 = velocity, defer = defer) {
    val velocity: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "velocity:" to 10, velocity to 5,
        "defer:" to 10, defer to 5
    )
}

class NoteOff(channel: Int, note: MidiNote, velocity: Int = 0, defer: Int = 0) :
    NoteEvent(NOTE_OFF, channel, note, data2 = velocity, defer = defer) {
    val velocity: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "velocity:" to 10, velocity to 5,
        "defer:" to 10, defer to 5
    )
}

class PitchBend(channel: Int, val bend: Int, defer: Int = 0) :
    MidiEvent(PITCH_BEND, channel, data1 = bend and 0b0111_1111, data2 = (bend shr 7) and 0b0111_1111, defer = defer) {

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "bend:" to 10, bend to 5,
        "defer:" to 10, defer to 5
    )
}

class Aftertouch(channel: Int, pressure: Int = 0, defer: Int = 0) :
    MidiEvent(AFTERTOUCH, channel, data1 = pressure, defer = defer) {
    val pressure: Int get() = data1

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "pressure:" to 10, pressure to 5,
        "defer:" to 10, defer to 5
    )
}

class PolyAftertouch(channel: Int, note: MidiNote, pressure: Int = 0, defer: Int = 0) :
    NoteEvent(POLY_AFTERTOUCH, channel, note, data2 = pressure, defer = defer) {
    val pressure: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "pressure:" to 10, pressure to 3,
        "defer:" to 10, defer to 5
    )
}

class ControlChange(channel: Int, control: Int, value: Int, defer: Int = 0) :
    MidiEvent(CONTROL_CHANGE, channel, data1 = control, data2 = value, defer = defer) {
    val control: Int get() = data1
    val value: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "control:" to 10, control to 5,
        "value:" to 10, value to 5,
        "defer:" to 10, defer to 5
    )
}

class ProgramChange(channel: Int, program: Int, defer: Int = 0) :
    MidiEvent(PROGRAM_CHANGE, channel, data1 = program, defer = defer) {
    val program: Int get() = data1

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "program:" to 10, program to 5,
        "defer:" to 10, defer to 5
    )
}

class ClockTick : SysrtEvent(SYSRT_CLOCK)

class StartPlay : SysrtEvent(SYSRT_START)

class ContinuePlay : SysrtEvent(SYSRT_CONTINUE)

class StopPlay : SysrtEvent(SYSRT_STOP)

class Sensing : SysrtEvent(SYSRT_SENSING)

class ResetPlay : SysrtEvent(SYSRT_RESET)

object NOOPEvent : MidiEvent(NOOP) {
    override fun toString() = justify(
        type to -15,
        "defer:" to 10, defer to 5
    )
}

@ExperimentalUnsignedTypes
class SysEx(val id: Int, val sysexData: UByteArray) : MidiEvent(SYSEX) {
    constructor(id: Int, size: Int, init: (Int) -> UByte) : this(id, UByteArray(size, init))
}

