package midi.api

import midi.api.EventType.*
import midi.api.MidiNote.Companion.noteByNumber
import midi.core.justify

interface MidiEvent

sealed class ProtocolEvent(
    val data1: Int = 0,
    val data2: Int = 0,
    val channel: Int = 0,
    val type: EventType
) : MidiEvent {
    val data0 = (type.code + (channel and 0b0000_1111)) and 0b1111_1111

    open infix fun matches(other: MidiEvent) =
        other is ProtocolEvent && channel == other.channel && data0 == other.data0 && data1 == other.data1

    override fun toString() =
        "$type\tchannel:$channel\tdata1:$data1\tdata2:$data2"

    open fun copy(
        data1: Int = this.data1,
        data2: Int = this.data2,
        channel: Int = this.channel,
    ) = when (this.type) {
        NOTE_ON -> NoteOn(noteByNumber[data1], data2, channel)
        NOTE_OFF -> NoteOff(noteByNumber[data1], data2, channel)
        CONTROL_CHANGE -> ControlChange(data1, data2, channel)
        else -> NOOPEvent // TODO: push implementations into subclasses
    }
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
        fun fromHead(bits: Int) = when (bits and 0b1111_0000) {
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
            else -> NOOP
        }
    }
}

sealed class SysrtEvent(type: EventType) : ProtocolEvent(type = type) {
    override fun toString() = justify(
        type to -15,
    )
}

sealed class NoteEvent(
    val note: MidiNote,
    data2: Int = 0,
    channel: Int = 0,
    type: EventType = NOOP
) : ProtocolEvent(data1 = note.number, data2 = data2, channel, type) {
    constructor(
        note: Int,
        data2: Int = 0,
        channel: Int = 0,
        type: EventType = NOOP
    ) : this(noteByNumber[note], data2, channel, type)
}


class NoteOn(note: MidiNote, velocity: Int = 127, channel: Int = 0) :
    NoteEvent(note, data2 = velocity, channel, NOTE_ON) {
    val velocity: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "velocity:" to 10, velocity to 5,
    )
}

class NoteOff(note: MidiNote, velocity: Int = 0, channel: Int = 0) :
    NoteEvent(note, data2 = velocity, channel, NOTE_OFF) {
    val velocity: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "velocity:" to 10, velocity to 5,
    )
}

class PitchBend(val bend: Int, channel: Int = 0) :
    ProtocolEvent(
        data1 = bend and 0b0111_1111,
        data2 = (bend shr 7) and 0b0111_1111,
        channel,
        PITCH_BEND
    ) {

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "bend:" to 10, bend to 5,
    )
}

class Aftertouch(pressure: Int = 0, channel: Int = 0) :
    ProtocolEvent(data1 = pressure, channel = channel, type = AFTERTOUCH) {
    val pressure: Int get() = data1

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "pressure:" to 10, pressure to 5,
    )
}

class PolyAftertouch(note: MidiNote, pressure: Int = 0, channel: Int = 0) :
    NoteEvent(note, data2 = pressure, channel, POLY_AFTERTOUCH) {
    val pressure: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "note:" to 10, note to 5,
        "pressure:" to 10, pressure to 3,
    )
}

class ControlChange(control: Int, value: Int, channel: Int = 0) :
    ProtocolEvent(data1 = control, data2 = value, channel, CONTROL_CHANGE) {
    val control: Int get() = data1
    val value: Int get() = data2

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "control:" to 10, control to 5,
        "value:" to 10, value to 5,
    )
}

class ProgramChange(program: Int, channel: Int = 0) :
    ProtocolEvent(data1 = program, channel = channel, type = PROGRAM_CHANGE) {
    val program: Int get() = data1

    override fun toString() = justify(
        type to -15,
        "channel:" to 10, channel to 5,
        "program:" to 10, program to 5,
    )
}

class ClockTick : SysrtEvent(SYSRT_CLOCK)

class StartPlay : SysrtEvent(SYSRT_START)

class ContinuePlay : SysrtEvent(SYSRT_CONTINUE)

class StopPlay : SysrtEvent(SYSRT_STOP)

class Sensing : SysrtEvent(SYSRT_SENSING)

class ResetPlay : SysrtEvent(SYSRT_RESET)

object NOOPEvent : ProtocolEvent(type = NOOP) {
    override fun toString() = justify(
        type to -15,
    )
}

@ExperimentalUnsignedTypes
class SysEx(val id: Int, val sysexData: UByteArray) : ProtocolEvent(type = SYSEX) {
    constructor(id: Int, size: Int, init: (Int) -> UByte) : this(id, UByteArray(size, init))
}

