package midi.core

import midi.api.*

fun MidiEvent.isNote(note: MidiNote) = this is NoteEvent && this.note == note
fun MidiEvent.isNote(channel: Int, note: MidiNote) = this is NoteEvent && this.channel == channel && this.note == note

fun MidiEvent.isNoteOn(note: MidiNote) = this is NoteOn && this.note == note
fun MidiEvent.isNoteOn(channel: Int, note: MidiNote) = this is NoteOn && this.channel == channel && this.note == note

fun MidiEvent.isNoteOff(note: MidiNote) = this is NoteOff && this.note == note
fun MidiEvent.isNoteOff(channel: Int, note: MidiNote) = this is NoteOff && this.channel == channel && this.note == note


