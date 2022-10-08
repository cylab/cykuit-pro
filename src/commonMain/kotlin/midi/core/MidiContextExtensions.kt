package midi.core

import midi.api.*

fun MidiContext.note(note: MidiNote, length: Int, velocity: Int = 127, defer: Int = 0) {
    noteOn(note, velocity, defer)
    noteOff(note, velocity, defer + length)
}

fun MidiContext.noteOn(note: MidiNote, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(note, velocity, channel), defer)

fun MidiContext.noteOn(note: Int, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(MidiNote.noteByNumber[note], velocity, channel), defer)

fun MidiContext.noteOff(note: MidiNote, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(note, velocity, channel), defer)

fun MidiContext.noteOff(note: Int, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(MidiNote.noteByNumber[note], velocity, channel), defer)

fun MidiContext.pitchBend(bend: Int, defer: Int = 0) =
    emit(PitchBend(bend, channel), defer)

fun MidiContext.aftertouch(pressure: Int = 0, defer: Int = 0) =
    emit(Aftertouch(pressure, channel), defer)

fun MidiContext.polyAftertouch(note: MidiNote, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(note, pressure, channel), defer)

fun MidiContext.polyAftertouch(note: Int, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(MidiNote.noteByNumber[note], pressure, channel), defer)

fun MidiContext.controlChange(control: Int, value: Int, defer: Int = 0) =
    emit(ControlChange(control, value, channel), defer)

fun MidiContext.programChange(program: Int, defer: Int = 0) =
    emit(ProgramChange(program, channel), defer)

@ExperimentalUnsignedTypes
fun MidiContext.sysEx(id: Int, sysexData: UByteArray) =
    emit(SysEx(id, sysexData))

fun MidiContext.sysEx(id: Int, size: Int, init: (Int) -> UByte) =
    emit(SysEx(id, size, init))
