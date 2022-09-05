package midi.core

import midi.api.*

fun MidiContext.note(note: MidiNote, length: Int, velocity: Int = 127, defer: Int = 0) {
    noteOn(note, velocity, defer)
    noteOff(note, velocity, defer + length)
}

fun MidiContext.noteOn(note: MidiNote, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(note, velocity, defer, channel))

fun MidiContext.noteOn(note: Int, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(MidiNote.noteByNumber[note], velocity, defer, channel))

fun MidiContext.noteOff(note: MidiNote, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(note, velocity, defer, channel))

fun MidiContext.noteOff(note: Int, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(MidiNote.noteByNumber[note], velocity, defer, channel))

fun MidiContext.pitchBend(bend: Int, defer: Int = 0) =
    emit(PitchBend(bend, defer, channel))

fun MidiContext.aftertouch(pressure: Int = 0, defer: Int = 0) =
    emit(Aftertouch(pressure, defer, channel))

fun MidiContext.polyAftertouch(note: MidiNote, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(note, pressure, defer, channel))

fun MidiContext.polyAftertouch(note: Int, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(MidiNote.noteByNumber[note], pressure, defer, channel))

fun MidiContext.controlChange(control: Int, value: Int, defer: Int = 0) =
    emit(ControlChange(control, value, defer, channel))

fun MidiContext.programChange(program: Int, defer: Int = 0) =
    emit(ProgramChange(program, defer, channel))

@ExperimentalUnsignedTypes
fun MidiContext.sysEx(id: Int, sysexData: UByteArray) =
    emit(SysEx(id, sysexData))

fun MidiContext.sysEx(id: Int, size: Int, init: (Int) -> UByte) =
    emit(SysEx(id, size, init))
