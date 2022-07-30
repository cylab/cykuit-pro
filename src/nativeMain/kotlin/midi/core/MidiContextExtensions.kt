package midi.core

import midi.api.*

fun MidiContext.note(note: MidiNote, length: Int, velocity: Int = 127, defer: Int = 0) {
    noteOn(note, velocity, defer)
    noteOff(note, velocity, defer + length)
}

fun MidiContext.noteOn(note: MidiNote, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(channel, note, velocity, defer))

fun MidiContext.noteOn(note: Int, velocity: Int = 127, defer: Int = 0) =
    emit(NoteOn(channel, MidiNote.noteByNumber[note], velocity, defer))

fun MidiContext.noteOff(note: MidiNote, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(channel, note, velocity, defer))

fun MidiContext.noteOff(note: Int, velocity: Int = 0, defer: Int = 0) =
    emit(NoteOff(channel, MidiNote.noteByNumber[note], velocity, defer))

fun MidiContext.pitchBend(bend: Int, defer: Int = 0) =
    emit(PitchBend(channel, bend, defer))

fun MidiContext.aftertouch(pressure: Int = 0, defer: Int = 0) =
    emit(Aftertouch(channel, pressure, defer))

fun MidiContext.polyAftertouch(note: MidiNote, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(channel, note, pressure, defer))

fun MidiContext.polyAftertouch(note: Int, pressure: Int = 0, defer: Int = 0) =
    emit(PolyAftertouch(channel, MidiNote.noteByNumber[note], pressure, defer))

fun MidiContext.controlChange(control: Int, value: Int, defer: Int = 0) =
    emit(ControlChange(channel, control, value, defer))

fun MidiContext.programChange(program: Int, defer: Int = 0) =
    emit(ProgramChange(channel, program, defer))

@ExperimentalUnsignedTypes
fun MidiContext.sysEx(sysexData: UByteArray) =
    emit(SysEx(sysexData))
